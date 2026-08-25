package com.dfgcontroller.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import com.dfgcontroller.ui.MainViewModel
import com.dfgcontroller.ui.components.AnimatedKernelSwitch
import com.dfgcontroller.ui.components.DashboardHeroCard
import com.dfgcontroller.ui.theme.DarkBackground
import com.dfgcontroller.ui.theme.DarkSurface
import com.dfgcontroller.ui.theme.ElectricCyan
import com.dfgcontroller.ui.theme.ErrorRed
import com.dfgcontroller.ui.theme.SuccessGreen
import com.dfgcontroller.ui.theme.neonGlow

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val accentColor = MaterialTheme.colorScheme.primary
    val kernelVersion by viewModel.kernelVersion.collectAsState()
    val currentGovernor by viewModel.currentGovernor.collectAsState()
    val currentScheduler by viewModel.currentScheduler.collectAsState()
    val gamingMode by viewModel.gamingMode.collectAsState()
    val isApplyingProfile by viewModel.isApplyingProfile.collectAsState()
    val thermalTemperature by viewModel.thermalTemperature.collectAsState()
    val isThermalCritical by viewModel.isThermalCritical.collectAsState()
    val isLegacyKernel by viewModel.isLegacyKernel.collectAsState()
    val autoFastChargeActive by viewModel.autoFastChargeActive.collectAsState()
    val currentProfileName by viewModel.currentProfileName.collectAsState()
    val updateManifest by viewModel.updateManifest.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    
    val kernelUpdateVariant by viewModel.kernelUpdateVariant.collectAsState()
    val kernelUpdateStatus by viewModel.kernelUpdateStatus.collectAsState()

    val context = LocalContext.current

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically { 20 },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                
                item {
                    DashboardHeroCard(
                        kernelVersion = kernelVersion,
                        currentGovernor = currentGovernor,
                        currentScheduler = currentScheduler,
                        thermalTemp = thermalTemperature,
                        isThermalCritical = isThermalCritical,
                        modifier = Modifier.neonGlow(accentColor.copy(alpha = 0.1f))
                    )
                }

                item {
                    if (isLegacyKernel) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .neonGlow(ErrorRed.copy(alpha = 0.2f)),
                            colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val infiniteTransition = rememberInfiniteTransition(label = "warning")
                                val warnAlpha by infiniteTransition.animateFloat(
                                    initialValue = 0.4f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(800),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "warnAlpha"
                                )
                                Icon(
                                    Icons.Default.Warning, 
                                    contentDescription = null, 
                                    tint = ErrorRed.copy(alpha = warnAlpha)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "LEGACY KERNEL DETECTED: Unified API nodes unavailable.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ErrorRed
                                )
                            }
                        }
                    }
                }

                item {
                    com.dfgcontroller.ui.components.ProfileSelector(
                        currentProfile = currentProfileName,
                        onProfileSelected = viewModel::setKernelProfile
                    )
                }

                item {
                    var visibleAuto by remember { mutableStateOf(false) }
                    LaunchedEffect(autoFastChargeActive) {
                        visibleAuto = if (autoFastChargeActive) {
                            delay(200.milliseconds)
                            true
                        } else {
                            false
                        }
                    }
                    AnimatedVisibility(
                        visible = visibleAuto,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        AutoFastChargeBadge(accentColor)
                    }
                }

                item {
                    var visibleGaming by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(300)
                        visibleGaming = true
                    }
                    AnimatedVisibility(
                        visible = visibleGaming,
                        enter = slideInHorizontally { -it } + fadeIn()
                    ) {
                    GamingModeCard(
                        enabled = gamingMode,
                        accentColor = accentColor,
                        onToggle = viewModel::toggleGamingMode
                    )
                    }
                }

                item {
                    DiagnosticsCard(viewModel = viewModel, accentColor = accentColor)
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }

        AnimatedVisibility(
            visible = isApplyingProfile,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Card(
                modifier = Modifier.padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = accentColor),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                accentColor = accentColor,
                onDismiss = { viewModel.dismissUpdate() },
                onUpdate = { viewModel.startUpdate(context) }
            )
        }

        kernelUpdateVariant?.let { variant ->
            KernelUpdateDialog(
                variant = variant,
                status = kernelUpdateStatus,
                accentColor = accentColor,
                onUpdate = { viewModel.startKernelUpdate(context) }
            )
        }
    }
}

@Composable
fun KernelUpdateDialog(
    variant: com.dfgcontroller.core.KernelVariant,
    status: String?,
    accentColor: Color,
    onUpdate: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, accentColor)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "KERNEL UPDATE AVAILABLE",
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "DaisyForGaming ${variant.version_name}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                
                if (status != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = status, 
                        color = if (status.contains("Verified")) SuccessGreen else accentColor, 
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    if (status.contains("Verified")) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Please reboot to TWRP to flash the ZIP manually.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onUpdate,
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        enabled = (status == null || status.contains("Error"))
                    ) {
                        Text("DOWNLOAD & VERIFY", color = DarkBackground)
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateDialog(
    manifest: com.dfgcontroller.core.UpdateManifest,
    status: String?,
    accentColor: Color,
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
                color = if (manifest.mandatory) ErrorRed else accentColor
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = if (manifest.mandatory) "MANDATORY UPDATE" else "UPDATE AVAILABLE",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (manifest.mandatory) ErrorRed else accentColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Version ${manifest.latest_version_name}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                
                if (status != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = status, color = accentColor, style = MaterialTheme.typography.bodyMedium)
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
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        enabled = (status == null || status.startsWith("Error"))
                    ) {
                        Text("DOWNLOAD & INSTALL", color = DarkBackground)
                    }
                }
            }
        }
    }
}

@Composable
fun AutoFastChargeBadge(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = alpha)),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AUTO FAST-CHARGE ACTIVE",
                style = MaterialTheme.typography.labelMedium,
                color = accentColor
            )
        }
    }
}

@Composable
fun GamingModeCard(enabled: Boolean, accentColor: Color, onToggle: (Boolean) -> Unit) {
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
                    color = accentColor
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

@Composable
fun DiagnosticsCard(viewModel: MainViewModel, accentColor: Color) {
    var nodesFound by remember { mutableIntStateOf(0) }
    var totalNodes by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        val paths = listOf(
            com.dfgcontroller.core.SysfsPaths.DFG_BASE,
            com.dfgcontroller.core.SysfsPaths.CPU_GOVERNOR,
            com.dfgcontroller.core.SysfsPaths.THERMAL_STATUS,
            com.dfgcontroller.core.SysfsPaths.CPU_MIN_FREQ,
            com.dfgcontroller.core.SysfsPaths.CPU_MAX_FREQ
        )
        totalNodes = paths.size
        var count = 0
        paths.forEach { path ->
            if (com.topjohnwu.superuser.Shell.cmd("ls $path").exec().isSuccess) {
                count++
            }
        }
        nodesFound = count
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "KERNEL INTERFACE STATUS",
                style = MaterialTheme.typography.labelMedium,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { if (totalNodes > 0) nodesFound.toFloat() / totalNodes.toFloat() else 0f },
                    modifier = Modifier.weight(1f).height(8.dp).clip(CircleShape),
                    color = if (nodesFound == totalNodes) accentColor else ErrorRed,
                    trackColor = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$nodesFound / $totalNodes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (nodesFound == totalNodes) "All DFG kernel nodes detected." else "Some kernel nodes are missing. Features may be limited.",
                style = MaterialTheme.typography.bodySmall,
                color = if (nodesFound == totalNodes) Color.Gray else ErrorRed
            )
        }
    }
}
