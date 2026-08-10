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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.core.graphics.drawable.toBitmap
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface
import com.daisyforgaming.ui.theme.DarkBackground
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectorScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val apps by viewModel.installedApps.collectAsState()
    val isLoading by viewModel.isAppListLoading.collectAsState()
    val selectedPackage by viewModel.bypassTriggerPackage.collectAsState()
    val showSystemApps by viewModel.showSystemApps.collectAsState()
    
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
                title = { Text("Select Trigger App", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("System", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Switch(
                            checked = showSystemApps,
                            onCheckedChange = { viewModel.setShowSystemApps(it) },
                            modifier = Modifier.scale(0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search apps...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricCyan,
                    cursorColor = ElectricCyan
                ),
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
                        var isVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(index * 30L)
                            isVisible = true
                        }
                        
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn() + slideInVertically { 20 }
                        ) {
                            AppCard(
                                app = app,
                                isSelected = app.packageName == selectedPackage,
                                onClick = {
                                    viewModel.setBypassTriggerPackage(app.packageName)
                                    onBack()
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
fun AppCard(app: com.daisyforgaming.ui.models.AppInfo, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = app.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(text = app.packageName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

// Extension to scale components easily
fun Modifier.scale(scale: Float) = this.then(Modifier.graphicsLayer(scaleX = scale, scaleY = scale))
