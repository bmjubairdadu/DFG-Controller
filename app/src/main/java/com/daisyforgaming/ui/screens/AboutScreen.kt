package com.daisyforgaming.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daisyforgaming.R
import com.daisyforgaming.BuildConfig
import kotlinx.coroutines.delay

@Composable
fun AboutScreen(
    onNavigateToCompatibility: () -> Unit, 
    onNavigateToPackages: () -> Unit,
    onNavigateToLogs: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToCustomization: () -> Unit
) {
    val accentColor = MaterialTheme.colorScheme.primary
    
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 2 }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "DFG Logo",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "DFG Controller",
                    style = MaterialTheme.typography.headlineMedium,
                    color = accentColor,
                    fontFamily = com.daisyforgaming.ui.theme.OrbitronFamily
                )
                Text(
                    text = "Version ${BuildConfig.VERSION_NAME} (DaisyForGaming)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AboutButton("KERNEL COMPATIBILITY INFO", accentColor, onNavigateToCompatibility)
                    AboutButton("SYSTEM PACKAGES", accentColor, onNavigateToPackages)
                    AboutButton("KERNEL LOGS", accentColor, onNavigateToLogs)
                    AboutButton("GET HELP & DOCUMENTATION", accentColor, onNavigateToHelp)
                    AboutButton("APP CUSTOMIZATION", accentColor, onNavigateToCustomization)
                }
            }
            
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AboutInfoCard(
                        title = "KERNEL CONTROL",
                        description = "Advanced tuning for rooted Android devices. Optimized for gaming performance.",
                        accentColor = accentColor
                    )
                    
                    AboutInfoCard(
                        title = "DEVELOPER",
                        description = "DaisyForGaming Kernel Team",
                        accentColor = accentColor
                    )
                }
            }
            
            item {
                Text(
                    text = "© 2026 DaisyForGaming. All rights reserved.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun AboutButton(text: String, accentColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, color = accentColor)
    }
}

@Composable
fun AboutInfoCard(title: String, description: String, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = com.daisyforgaming.ui.theme.DarkSurface),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = accentColor,
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
