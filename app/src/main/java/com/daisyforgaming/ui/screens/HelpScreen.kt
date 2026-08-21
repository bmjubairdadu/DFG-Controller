package com.daisyforgaming.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.theme.DarkBackground
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit) {
    val accentColor = MaterialTheme.colorScheme.primary
    
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KERNEL HELP", color = accentColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + expandVertically()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HelpItem(
                        title = "CPU Governor",
                        description = "Determines how the kernel scales CPU frequency in response to load.\n- Schedutil: Balanced and modern.\n- Performance: Always max frequency.\n- Powersave: Always min frequency.",
                        accentColor = accentColor
                    )
                }
                item {
                    HelpItem(
                        title = "I/O Scheduler",
                        description = "Determines the order in which disk I/O requests are processed.\n- noop: Best for flash storage (SSD/EMMC).\n- deadline: Good for low latency.",
                        accentColor = accentColor
                    )
                }
                item {
                    HelpItem(
                        title = "Safe Frequency Ranges",
                        description = "For Mi A2 Lite (Daisy):\n- Min: 652 MHz\n- Max: 2016 MHz\nAvoid setting Max too low as it can cause system lag.",
                        accentColor = accentColor
                    )
                }
                item {
                    HelpItem(
                        title = "Thermal Throttling",
                        description = "The app automatically blocks high-performance profiles if the device temperature exceeds 60°C to prevent hardware damage.",
                        accentColor = accentColor
                    )
                }
            }
        }
    }
}

@Composable
fun HelpItem(title: String, description: String, accentColor: Color) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = accentColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.DarkGray)
    }
}
