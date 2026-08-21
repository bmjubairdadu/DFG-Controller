package com.dfgcontroller.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dfgcontroller.ui.MainViewModel
import com.dfgcontroller.ui.components.AnimatedKernelSwitch
import com.dfgcontroller.ui.components.GlowSlider
import com.dfgcontroller.ui.theme.ElectricCyan
import com.dfgcontroller.ui.theme.DarkSurface

@Composable
fun DisplayScreen(viewModel: MainViewModel) {
    val accentColor = MaterialTheme.colorScheme.primary
    val r by viewModel.kcalR.collectAsState()
    val g by viewModel.kcalG.collectAsState()
    val b by viewModel.kcalB.collectAsState()
    val enabled by viewModel.kcalEnabled.collectAsState()
    val dpiInfo by viewModel.dpiInfo.collectAsState()
    

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }
        
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "KCAL ENABLE",
                    style = MaterialTheme.typography.titleLarge,
                    color = accentColor,
                    modifier = Modifier.weight(1f)
                )
                AnimatedKernelSwitch(checked = enabled, onCheckedChange = { viewModel.setKcalEnabled(it) })
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ColorPreview(r, g, b)
                    Spacer(modifier = Modifier.height(24.dp))
                    KcalSlider(label = "RED", value = r, color = Color.Red, accentColor = accentColor) { viewModel.updateKcal(it, g, b) }
                    KcalSlider(label = "GREEN", value = g, color = Color.Green, accentColor = accentColor) { viewModel.updateKcal(r, it, b) }
                    KcalSlider(label = "BLUE", value = b, color = Color.Blue, accentColor = accentColor) { viewModel.updateKcal(r, g, it) }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.resetKcal() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurface)
            ) {
                Text("RESET KCAL", color = accentColor)
            }
        }

        item {
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
        }

        item {
            Text(
                text = "RESOLUTION CONTROL",
                style = MaterialTheme.typography.headlineSmall,
                color = accentColor
            )
        }

        item {
            ResolutionCard(
                accentColor = accentColor,
                onReset = { viewModel.resetResolution() },
                onSet = { w, h -> viewModel.setResolution(w, h) }
            )
        }

        item {
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
        }

        item {
            Text(
                text = "DPI / DENSITY CONTROL",
                style = MaterialTheme.typography.headlineSmall,
                color = accentColor
            )
        }

        item {
            DpiControlCard(
                info = dpiInfo,
                accentColor = accentColor,
                onReset = { viewModel.resetDpi() },
                onApply = { viewModel.setDpi(it) }
            )
        }
        
        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun DpiControlCard(info: String, accentColor: Color, onReset: () -> Unit, onApply: (Int) -> Unit) {
    var textValue by remember { mutableStateOf("") }
    // Try to extract current DPI from info string for slider default
    val currentDpi = remember(info) {
        info.split("\n")
            .firstOrNull { it.contains("density") }
            ?.split(" ")
            ?.lastOrNull()
            ?.toIntOrNull() ?: 480
    }
    
    var sliderValue by remember(currentDpi) { mutableFloatStateOf(currentDpi.toFloat()) }

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "CURRENT STATUS:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = info, style = MaterialTheme.typography.bodySmall, color = Color.White)
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TARGET DPI: ${sliderValue.toInt()}",
                style = MaterialTheme.typography.labelMedium,
                color = accentColor
            )
            GlowSlider(
                value = sliderValue,
                onValueChange = { 
                    sliderValue = it
                    textValue = it.toInt().toString()
                },
                valueRange = 160f..720f,
                accentColor = accentColor
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { 
                        if (it.all { char -> char.isDigit() }) {
                            textValue = it
                            it.toIntOrNull()?.let { v -> sliderValue = v.toFloat().coerceIn(160f, 720f) }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Manual Input") },
                    placeholder = { Text("e.g. 480") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = { 
                        val target = textValue.toIntOrNull() ?: sliderValue.toInt()
                        onApply(target)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.Black)
                ) {
                    Text("APPLY")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onReset, 
                modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.DarkGray)
            ) {
                Text("RESET TO DEFAULT", color = accentColor)
            }
        }
    }
}

@Composable
fun ResolutionCard(accentColor: Color, onReset: () -> Unit, onSet: (Int, Int) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Scaling can improve gaming performance in heavy titles.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onSet(720, 1280) }, modifier = Modifier.weight(1f)) { Text("720p") }
                Button(onClick = { onSet(1080, 1920) }, modifier = Modifier.weight(1f)) { Text("1080p") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) { Text("RESET TO NATIVE", color = accentColor) }
        }
    }
}

@Composable
fun ColorPreview(r: Float, g: Float, b: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(r.toInt().coerceIn(0, 255), g.toInt().coerceIn(0, 255), b.toInt().coerceIn(0, 255)))
    )
}

@Composable
fun KcalSlider(label: String, value: Float, color: Color, accentColor: Color, onValueChange: (Float) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
            Text(text = value.toInt().toString(), color = accentColor, style = MaterialTheme.typography.labelMedium)
        }
        GlowSlider(value = value, onValueChange = onValueChange, accentColor = color)
        Spacer(modifier = Modifier.height(16.dp))
    }
}
