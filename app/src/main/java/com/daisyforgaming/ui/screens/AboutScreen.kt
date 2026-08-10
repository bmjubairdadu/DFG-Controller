package com.daisyforgaming.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daisyforgaming.R
import com.daisyforgaming.ui.theme.ElectricCyan

@Composable
fun AboutScreen(onNavigateToCompatibility: () -> Unit, onNavigateToPackages: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "DFG Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "DFG Controller",
                style = MaterialTheme.typography.headlineMedium,
                color = ElectricCyan,
                fontFamily = com.daisyforgaming.ui.theme.OrbitronFamily
            )
            Text(
                text = "Version 1.0.0 (DaisyForGaming)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onNavigateToCompatibility,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("KERNEL COMPATIBILITY INFO", color = ElectricCyan)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onNavigateToPackages,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SYSTEM PACKAGES", color = ElectricCyan)
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            AboutInfoCard(
                title = "KERNEL CONTROL",
                description = "Advanced tuning for rooted Android devices. Optimized for gaming performance."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            AboutInfoCard(
                title = "DEVELOPER",
                description = "DaisyForGaming Kernel Team"
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "© 2026 DaisyForGaming. All rights reserved.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AboutInfoCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = com.daisyforgaming.ui.theme.DarkSurface),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = ElectricCyan,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}
