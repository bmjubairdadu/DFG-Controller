package com.daisyforgaming.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.MainViewModel
import com.daisyforgaming.ui.components.AnimatedSegmentedControl
import com.daisyforgaming.ui.theme.ElectricCyan

@Composable
fun CpuIoScreen(viewModel: MainViewModel) {
    val currentGovernor by viewModel.currentGovernor.collectAsState()
    val availableGovernors by viewModel.availableGovernors.collectAsState()
    val currentScheduler by viewModel.currentScheduler.collectAsState()

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
    }
}
