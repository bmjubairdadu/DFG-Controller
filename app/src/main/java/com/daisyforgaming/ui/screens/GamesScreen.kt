package com.daisyforgaming.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface
import com.daisyforgaming.ui.theme.DarkBackground
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val apps by viewModel.installedApps.collectAsState()
    val isLoading by viewModel.isAppListLoading.collectAsState()
    val gameApps by viewModel.gameApps.collectAsState()
    val killWhitelist by viewModel.killWhitelist.collectAsState()
    
    var tabIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredApps = remember(apps, searchQuery) {
        if (searchQuery.isEmpty()) apps else apps.filter { 
            it.name.contains(searchQuery, ignoreCase = true) || it.packageName.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshAppList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Mode Manager", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SecondaryTabRow(
                selectedTabIndex = tabIndex,
                containerColor = DarkBackground,
                contentColor = ElectricCyan,
                indicator = { 
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabIndex),
                        color = ElectricCyan
                    )
                }
            ) {
                Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }) {
                    Text("Games", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }) {
                    Text("Whitelist", modifier = Modifier.padding(16.dp))
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search apps...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricCyan),
                shape = RoundedCornerShape(12.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ElectricCyan)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(filteredApps) { index, app ->
                        val isSelected = if (tabIndex == 0) gameApps.contains(app.packageName) else killWhitelist.contains(app.packageName)
                        
                        var isVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(index * 20L)
                            isVisible = true
                        }
                        
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn() + slideInVertically { 20 }
                        ) {
                            MultiSelectAppCard(
                                app = app,
                                isSelected = isSelected,
                                onClick = {
                                    if (tabIndex == 0) viewModel.toggleGameApp(app.packageName)
                                    else viewModel.toggleWhitelistApp(app.packageName)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MultiSelectAppCard(app: com.daisyforgaming.ui.models.AppInfo, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ElectricCyan.copy(alpha = 0.1f) else DarkSurface
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan) else null
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = app.icon.toBitmap().asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = app.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(text = app.packageName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ElectricCyan)
            }
        }
    }
}
