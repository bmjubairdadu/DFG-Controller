package com.daisyforgaming.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.components.AnimatedKernelSwitch
import com.daisyforgaming.ui.components.AnimatedSegmentedControl
import com.daisyforgaming.ui.components.GlowSlider
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface

@Composable
fun CpuIoScreen(viewModel: MainViewModel) {
    val currentGovernor by viewModel.currentGovernor.collectAsState()
    val availableGovernors by viewModel.availableGovernors.collectAsState()
    val currentScheduler by viewModel.currentScheduler.collectAsState()
    
    val touchBoostEnabled by viewModel.touchBoostEnabled.collectAsState()
    val touchBoostDuration by viewModel.touchBoostDuration.collectAsState()
    val lmkAggressive by viewModel.lmkAggressive.collectAsState()

    var showAdvancedBoost by remember { mutableStateOf(false) }

    // Assuming a fixed list of schedulers for simplicity or we can read them too
    val availableSchedulers = listOf("cfq", "deadline", "noop", "zen")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }
        
        item {
            Text(
                text = "CPU GOVERNOR",
                style = MaterialTheme.typography.headlineSmall,
                color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (availableGovernors.isNotEmpty()) {
                AnimatedSegmentedControl(
                    options = availableGovernors,
                    selectedOption = currentGovernor,
                    onOptionSelected = { viewModel.setGovernor(it) },
                    modifier = Modifier.height(56.dp)
                )
            }
        }

        item {
            Text(
                text = "I/O SCHEDULER",
                style = MaterialTheme.typography.headlineSmall,
                color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedSegmentedControl(
                options = availableSchedulers,
                selectedOption = currentScheduler,
                onOptionSelected = { viewModel.setScheduler(it) },
                modifier = Modifier.height(56.dp)
            )
        }

        item {
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
        }

        item {
            Text(
                text = "SMOOTHNESS",
                style = MaterialTheme.typography.headlineSmall,
                color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Touch Boost
                SmoothnessToggleCard(
                    title = "Touch Boost",
                    description = "Temporarily boosts CPU frequencies on screen touch for improved responsiveness.",
                    checked = touchBoostEnabled,
                    onCheckedChange = { viewModel.setTouchBoostEnabled(it) }
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAdvancedBoost = !showAdvancedBoost }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Advanced Settings",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (showAdvancedBoost) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                        
                        AnimatedVisibility(visible = showAdvancedBoost) {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                Text(
                                    text = "Boost Duration: ${touchBoostDuration}ms",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricCyan
                                )
                                GlowSlider(
                                    value = touchBoostDuration.toFloat(),
                                    onValueChange = { viewModel.setTouchBoostDuration(it.toInt()) },
                                    valueRange = 20f..150f,
                                    accentColor = ElectricCyan
                                )
                                Text(
                                    text = "Kernel range: 20ms - 150ms",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }

                // Smart Memory Management
                SmoothnessToggleCard(
                    title = "Smart Memory Management",
                    description = "Frees background app memory a bit sooner for smoother multitasking. Apps you switch back to may need to reload occasionally.",
                    checked = lmkAggressive,
                    onCheckedChange = { viewModel.setLmkAggressive(it) }
                )
            }
        }
        
        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun SmoothnessToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    extraContent: @Composable (() -> Unit)? = null
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(16.dp))
                AnimatedKernelSwitch(checked = checked, onCheckedChange = onCheckedChange)
            }
            extraContent?.invoke()
        }
    }
}
