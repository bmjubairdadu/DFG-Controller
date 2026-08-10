package com.daisyforgaming.ui.screens

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface
import com.daisyforgaming.ui.theme.DarkBackground
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val pm = context.packageManager
    
    var apps by remember { mutableStateOf<List<PackageInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var showSystem by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        apps = pm.getInstalledPackages(PackageManager.GET_META_DATA)
        isLoading = false
    }

    val filteredApps = remember(apps, searchQuery, showSystem) {
        apps.filter { pkg ->
            val appInfo = pkg.applicationInfo
            if (appInfo == null) return@filter false
            
            (showSystem || (appInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) &&
            (pkg.packageName.contains(searchQuery, ignoreCase = true) || 
             appInfo.loadLabel(pm).toString().contains(searchQuery, ignoreCase = true))
        }.sortedBy { it.applicationInfo?.loadLabel(pm)?.toString() ?: "" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Packages") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Show System", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Switch(
                            checked = showSystem,
                            onCheckedChange = { showSystem = it },
                            modifier = Modifier.scale(0.6f)
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
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Filter packages...") },
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
                    itemsIndexed(filteredApps) { index, pkg ->
                        var isVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(index * 10L)
                            isVisible = true
                        }
                        
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn() + slideInVertically { 20 }
                        ) {
                            PackageCard(pkg, pm)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PackageCard(pkg: PackageInfo, pm: PackageManager) {
    var expanded by remember { mutableStateOf(false) }
    val appInfo = pkg.applicationInfo ?: return

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    bitmap = appInfo.loadIcon(pm).toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appInfo.loadLabel(pm).toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = pkg.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = if (expanded) ElectricCyan else Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))
                
                PackageDetailRow("Version", pkg.versionName ?: "Unknown")
                PackageDetailRow("Target SDK", appInfo.targetSdkVersion.toString())
                PackageDetailRow("UID", appInfo.uid.toString())
                @Suppress("DEPRECATION")
                PackageDetailRow("Install Source", pm.getInstallerPackageName(pkg.packageName) ?: "Sideloaded")
            }
        }
    }
}

@Composable
fun PackageDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.labelSmall, color = ElectricCyan, fontWeight = FontWeight.Bold)
    }
}
