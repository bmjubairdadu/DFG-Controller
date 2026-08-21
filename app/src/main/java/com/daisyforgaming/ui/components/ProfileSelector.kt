package com.daisyforgaming.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daisyforgaming.ui.models.KernelProfile
import com.daisyforgaming.ui.theme.ElectricCyan

@Composable
fun ProfileSelector(
    currentProfile: String,
    onProfileSelected: (KernelProfile) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "KERNEL PROFILES",
            style = MaterialTheme.typography.labelMedium,
            color = ElectricCyan,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileButton("Battery", currentProfile == "Battery", Modifier.weight(1f)) {
                onProfileSelected(KernelProfile.Battery)
            }
            ProfileButton("Balanced", currentProfile == "Balanced", Modifier.weight(1f)) {
                onProfileSelected(KernelProfile.Balanced)
            }
            ProfileButton("Performance", currentProfile == "Performance", Modifier.weight(1f)) {
                onProfileSelected(KernelProfile.Performance)
            }
        }
    }
}

@Composable
fun ProfileButton(
    name: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) ElectricCyan else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = name, style = MaterialTheme.typography.labelSmall)
    }
}
