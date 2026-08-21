package com.daisyforgaming.ui.screens

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.shape.CircleShape
import com.daisyforgaming.ui.components.AnimatedSegmentedControl
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.components.AnimatedKernelSwitch
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface

@Composable
fun PowerScreen(viewModel: MainViewModel, onNavigateToAppSelector: () -> Unit, onNavigateToWakelocks: () -> Unit) {
    val accentColor = MaterialTheme.colorScheme.primary
    val fastCharge by viewModel.fastCharge.collectAsState()
    val dynamicFsync by viewModel.dynamicFsync.collectAsState()
    val bypassTriggerEnabled by viewModel.bypassTriggerEnabled.collectAsState()
    val bypassTriggerPackage by viewModel.bypassTriggerPackage.collectAsState()
    
    val chargePriority by viewModel.chargePriority.collectAsState()
    val autoFastChargeActive by viewModel.autoFastChargeActive.collectAsState()
    val tcpCongestion by viewModel.tcpCongestion.collectAsState()
    val availableTcpCongestions by viewModel.availableTcpCongestions.collectAsState()
    val memoryStats by viewModel.memoryStats.collectAsState()
    val zramEnabled by viewModel.zramEnabled.collectAsState()
    val zramSize by viewModel.zramSize.collectAsState()

    val context = LocalContext.current
    var hasUsageAccess by remember { mutableStateOf(checkUsageStatsPermission(context)) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }
        
        item {
            PowerToggleRow(
                label = "FORCE FAST CHARGE",
                description = "Increase charging current (use with caution)",
                checked = fastCharge,
                onCheckedChange = { viewModel.setFastCharge(it) },
                accentColor = accentColor
            )
        }

        item {
            PowerToggleRow(
                label = "DYNAMIC FSYNC",
                description = "Disable fsync while screen is on for better performance",
                checked = dynamicFsync,
                onCheckedChange = { viewModel.setDynamicFsync(it) },
                accentColor = accentColor
            )
        }

        item {
            PowerToggleRow(
                label = "FAST CHARGE PRIORITY",
                description = "Charges faster within safe limits. Does not affect how long a full charge lasts.",
                checked = chargePriority,
                onCheckedChange = { viewModel.setChargePriority(it) },
                accentColor = accentColor
            )
        }

        item {
            Button(
                onClick = onNavigateToWakelocks,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurface)
            ) {
                Text("OPEN WAKELOCK INSPECTOR", color = accentColor)
            }
        }

        if (autoFastChargeActive) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AUTO FAST-CHARGE ENGAGED", style = MaterialTheme.typography.labelMedium, color = accentColor)
                    }
                }
            }
        }

        item {
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
        }

        item {
            Text(text = "NETWORK TUNING", style = MaterialTheme.typography.headlineSmall, color = accentColor)
            Spacer(modifier = Modifier.height(12.dp))
            
            // Map available congestions to user-friendly labels if they exist
            val displayOptions = remember(availableTcpCongestions) {
                availableTcpCongestions.map {
                    when(it) {
                        "cubic" -> "Cubic (Default)"
                        "bbr" -> "BBR (Low Latency)"
                        else -> it.replaceFirstChar { char -> char.uppercase() }
                    }
                }
            }
            val selectedDisplay = when(tcpCongestion) {
                "cubic" -> "Cubic (Default)"
                "bbr" -> "BBR (Low Latency)"
                else -> tcpCongestion.replaceFirstChar { char -> char.uppercase() }
            }

            if (displayOptions.isNotEmpty()) {
                AnimatedSegmentedControl(
                    options = displayOptions,
                    selectedOption = selectedDisplay,
                    onOptionSelected = { selected ->
                        val actualAlgo = when(selected) {
                            "Cubic (Default)" -> "cubic"
                            "BBR (Low Latency)" -> "bbr"
                            else -> selected.lowercase()
                        }
                        viewModel.setTcpCongestion(actualAlgo)
                    },
                    modifier = Modifier.height(48.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "BBR can reduce latency and improve stability on congested or unstable networks (useful for gaming). It does not increase your raw internet speed — actual bandwidth still depends on your carrier or WiFi signal.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        item {
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
        }

        item {
            Text(text = "MEMORY & ZRAM", style = MaterialTheme.typography.headlineSmall, color = accentColor)
        }

        item {
            ZramControlCard(
                enabled = zramEnabled,
                size = zramSize,
                stats = memoryStats,
                onEnabledChange = { viewModel.setZramEnabled(it) },
                onSizeChange = { viewModel.setZramSize(it) },
                accentColor = accentColor
            )
        }

        item {
            MemoryActionCard(
                label = "Compact ZRAM",
                description = "Free up memory by compressing unused ZRAM blocks.",
                onAction = { viewModel.compactZram() },
                accentColor = accentColor
            )
        }

        item {
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
        }

        item {
            Text(
                text = "BYPASS CHARGING",
                style = MaterialTheme.typography.headlineSmall,
                color = accentColor
            )
        }

        if (!hasUsageAccess) {
            item {
                UsageAccessOnboardingCard {
                    context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
            }
        }

        item {
            BypassTriggerCard(
                enabled = bypassTriggerEnabled,
                packageName = bypassTriggerPackage,
                onEnabledChange = { viewModel.setBypassTriggerEnabled(it) },
                onSelectApp = onNavigateToAppSelector,
                accentColor = accentColor
            )
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun MemoryActionCard(label: String, description: String, accentColor: Color, onAction: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.titleLarge, color = accentColor)
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.2f))
            ) {
                Text("APPLY", color = accentColor)
            }
        }
    }
}

@Composable
fun UsageAccessOnboardingCard(onGrantClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCC00).copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCC00))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFCC00))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Permission Needed", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "To detect when your chosen app starts, DFG needs 'Usage Access'. This is required for App-Triggered Bypass Charging.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onGrantClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC00)),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("GRANT ACCESS", color = Color.Black)
            }
        }
    }
}

@Composable
fun BypassTriggerCard(
    enabled: Boolean,
    packageName: String?,
    accentColor: Color,
    onEnabledChange: (Boolean) -> Unit,
    onSelectApp: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("App-Triggered Bypass", style = MaterialTheme.typography.titleLarge, color = accentColor)
                    Text(
                        "Automatically engage bypass charging when a specific app is in foreground.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                AnimatedKernelSwitch(checked = enabled, onCheckedChange = onEnabledChange)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(onClick = onSelectApp)
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Target App", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Text(
                            text = packageName ?: "None selected",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (packageName != null) Color.White else Color.DarkGray
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }
}

fun checkUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName)
    } else {
        @Suppress("DEPRECATION")
        appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName)
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

@Composable
fun ZramControlCard(
    enabled: Boolean,
    size: String,
    stats: com.daisyforgaming.core.MemoryStats,
    accentColor: Color,
    onEnabledChange: (Boolean) -> Unit,
    onSizeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enable zRAM", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.weight(1f))
                AnimatedKernelSwitch(checked = enabled, onCheckedChange = onEnabledChange)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("DISK SIZE", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedSegmentedControl(
                options = listOf("256M", "512M", "1024M", "2048M"),
                selectedOption = size,
                onOptionSelected = onSizeChange,
                modifier = Modifier.height(40.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            MemoryUsageBar(label = "SYSTEM RAM", total = stats.totalRam, free = stats.freeRam, accentColor = accentColor)
            Spacer(modifier = Modifier.height(12.dp))
            MemoryUsageBar(label = "ZRAM SWAP", total = stats.totalZram, free = stats.freeZram, accentColor = accentColor)
        }
    }
}

@Composable
fun MemoryUsageBar(label: String, total: Long, free: Long, accentColor: Color) {
    val used = total - free
    val progress = if (total > 0) used.toFloat() / total.toFloat() else 0f
    
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text("${used / 1024}MB / ${total / 1024}MB", style = MaterialTheme.typography.labelSmall, color = accentColor)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = accentColor,
            trackColor = Color.DarkGray
        )
    }
}

@Composable
fun PowerToggleRow(label: String, description: String, checked: Boolean, accentColor: Color, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.titleLarge, color = accentColor)
            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
        AnimatedKernelSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
