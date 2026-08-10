package com.daisyforgaming.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.components.AnimatedKernelSwitch
import com.daisyforgaming.ui.components.DashboardHeroCard
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface
import com.daisyforgaming.ui.theme.DarkBackground
import com.daisyforgaming.ui.theme.ErrorRed

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val kernelVersion by viewModel.kernelVersion.collectAsState()
    val currentGovernor by viewModel.currentGovernor.collectAsState()
    val currentScheduler by viewModel.currentScheduler.collectAsState()
    val gamingMode by viewModel.gamingMode.collectAsState()
    val isApplyingProfile by viewModel.isApplyingProfile.collectAsState()
    val autoFastChargeActive by viewModel.autoFastChargeActive.collectAsState()
    val updateManifest by viewModel.updateManifest.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()

    val context = LocalContext.current

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically { 20 }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                
                item {
                    DashboardHeroCard(
                        kernelVersion = kernelVersion,
                        currentGovernor = currentGovernor,
                        currentScheduler = currentScheduler
                    )
                }

                if (autoFastChargeActive) {
                    item {
                        AutoFastChargeBadge()
                    }
                }

                item {
                    GamingModeCard(
                        enabled = gamingMode,
                        onToggle = { viewModel.toggleGamingMode(it) }
                    )
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }

        AnimatedVisibility(
            visible = isApplyingProfile,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Card(
                modifier = Modifier.padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = ElectricCyan),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Applying saved profile...",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = DarkSurface,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        updateManifest?.let { manifest ->
            UpdateDialog(
                manifest = manifest,
                status = updateStatus,
                onDismiss = { viewModel.dismissUpdate() },
                onUpdate = { viewModel.startUpdate(context) }
            )
        }
    }
}

@Composable
fun UpdateDialog(
    manifest: com.daisyforgaming.core.UpdateManifest,
    status: String?,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { if (!manifest.mandatory) onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp, 
                color = if (manifest.mandatory) ErrorRed else ElectricCyan
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = if (manifest.mandatory) "MANDATORY UPDATE" else "UPDATE AVAILABLE",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (manifest.mandatory) ErrorRed else ElectricCyan
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Version ${manifest.latest_version_name}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                
                if (status != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = status, color = ElectricCyan, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "CHANGELOG:",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    text = manifest.changelog,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    if (!manifest.mandatory) {
                        TextButton(onClick = onDismiss) {
                            Text("LATER", color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onUpdate,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        enabled = status == null || status.startsWith("Error")
                    ) {
                        Text("DOWNLOAD & INSTALL", color = DarkBackground)
                    }
                }
            }
        }
    }
}

@Composable
fun AutoFastChargeBadge() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = CardDefaults.cardColors(containerColor = ElectricCyan.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AUTO FAST-CHARGE ACTIVE",
                style = MaterialTheme.typography.labelMedium,
                color = ElectricCyan
            )
        }
    }
}

@Composable
fun GamingModeCard(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GAMING MODE",
                    style = MaterialTheme.typography.titleLarge,
                    color = ElectricCyan
                )
                Text(
                    text = if (enabled) "Extreme performance active" else "System default presets",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
            AnimatedKernelSwitch(checked = enabled, onCheckedChange = onToggle)
        }
    }
}
