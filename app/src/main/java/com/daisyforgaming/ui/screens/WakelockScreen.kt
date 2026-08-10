package com.daisyforgaming.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daisyforgaming.core.ShellManager
import com.daisyforgaming.core.SysfsPaths
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkBackground
import com.daisyforgaming.ui.theme.DarkSurface

data class WakelockInfo(
    val name: String,
    val count: String,
    val totalTime: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WakelockScreen(onBack: () -> Unit) {
    var wakelocks by remember { mutableStateOf<List<WakelockInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val raw = ShellManager.readSysfs(SysfsPaths.WAKELOCKS)
        if (raw != null) {
            // Very simple parser for /proc/wakelocks
            val lines = raw.split("\n").drop(1) // Drop header
            wakelocks = lines.mapNotNull { line ->
                val parts = line.split(Regex("\\s+")).filter { it.isNotBlank() }
                if (parts.size >= 3) {
                    WakelockInfo(parts[0], parts[1], parts[2])
                } else null
            }.sortedByDescending { it.count.toLongOrNull() ?: 0L }.take(20)
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wakelock Inspector") },
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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ElectricCyan)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "TOP BATTERY DRAIN SOURCES",
                        style = MaterialTheme.typography.labelMedium,
                        color = ElectricCyan,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                if (wakelocks.isEmpty()) {
                    item {
                        Text("No wakelock data available. Kernel might not expose /proc/wakelocks.", color = Color.Gray)
                    }
                }

                items(wakelocks) { info ->
                    WakelockCard(info)
                }
            }
        }
    }
}

@Composable
fun WakelockCard(info: WakelockInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = info.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(text = "Total Time: ${info.totalTime}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(text = info.count, style = MaterialTheme.typography.headlineSmall, color = ElectricCyan)
        }
    }
}
