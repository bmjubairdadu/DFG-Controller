package com.daisyforgaming.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.theme.DarkBackground
import com.daisyforgaming.ui.theme.DarkSurface
import com.daisyforgaming.ui.theme.ElectricCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val accentColor = MaterialTheme.colorScheme.primary
    val logs by viewModel.kernelLogs.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.refreshKernelLogs()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KERNEL LOGS", style = MaterialTheme.typography.titleLarge, color = accentColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Gray)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshKernelLogs() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = accentColor)
                    }
                    IconButton(onClick = {
                        viewModel.exportLogs(context) { path ->
                            if (path != null) {
                                // Simple toast for now, real app would show share sheet
                                android.widget.Toast.makeText(context, "Saved to $path", android.widget.Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Export", tint = accentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(logs) { log ->
                LogItem(log)
            }
        }
    }
}

@Composable
fun LogItem(text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                ),
                color = if (text.contains("crit", ignoreCase = true) || text.contains("err", ignoreCase = true)) Color.Red else Color.LightGray
            )
        }
    }
}
