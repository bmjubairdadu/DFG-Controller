package com.daisyforgaming.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkBackground
import com.daisyforgaming.ui.theme.DarkSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompatibilityScreen(onBack: () -> Unit) {
    val features = listOf(
        "Module Loading (LKM)" to "Full support for external kernel modules.",
        "init.d Support" to "Auto-run scripts on boot from /system/etc/init.d.",
        "KCAL Color Control" to "Advanced display calibration via sysfs.",
        "ZRAM & ZSmalloc" to "Compressed swap in RAM with optimized allocator.",
        "Game Mode Interlock" to "Direct communication between userspace and kernel for performance spikes.",
        "TCP BBR Support" to "Google's congestion control for optimized networking."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compatibility Info") },
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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "CONFIRMED KERNEL FEATURES",
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricCyan,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(features.size) { index ->
                val (title, desc) = features[index]
                FeatureCard(title, desc)
            }
        }
    }
}

@Composable
fun FeatureCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Green, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
