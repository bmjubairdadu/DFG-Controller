package com.daisyforgaming.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.components.AnimatedKernelSwitch
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface

@Composable
fun GpuScreen(viewModel: MainViewModel) {
    val gpuConservative by viewModel.gpuConservative.collectAsState()
    var showTooltip by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }
        
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "GPU CONSERVATIVE",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = ElectricCyan
                                )
                                IconButton(onClick = { showTooltip = !showTooltip }) {
                                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Gray)
                                }
                            }
                        }
                        AnimatedKernelSwitch(checked = gpuConservative, onCheckedChange = { viewModel.setGpuConservative(it) })
                    }
                    
                    AnimatedVisibility(
                        visible = showTooltip,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Text(
                            text = "Reduces GPU power consumption by being more 'conservative' with frequency ramps. Good for battery, might affect peak performance.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
