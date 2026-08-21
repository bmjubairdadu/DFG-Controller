package com.dfgcontroller.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.dfgcontroller.ui.models.KernelProfile
import com.dfgcontroller.ui.theme.ElectricCyan

@Composable
fun ProfileSelector(
    currentProfile: String,
    onProfileSelected: (KernelProfile) -> Unit
) {
    val accentColor = MaterialTheme.colorScheme.primary
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "KERNEL PROFILES",
            style = MaterialTheme.typography.labelMedium,
            color = accentColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileButton("Battery", currentProfile == "Battery", accentColor, Modifier.weight(1f)) {
                onProfileSelected(KernelProfile.Battery)
            }
            ProfileButton("Balanced", currentProfile == "Balanced", accentColor, Modifier.weight(1f)) {
                onProfileSelected(KernelProfile.Balanced)
            }
            ProfileButton("Performance", currentProfile == "Performance", accentColor, Modifier.weight(1f)) {
                onProfileSelected(KernelProfile.Performance)
            }
        }
    }
}

@Composable
fun ProfileButton(
    name: String,
    isSelected: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        label = "buttonScale"
    )
    
    Button(
        onClick = onClick,
        modifier = modifier.scale(animatedScale),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) accentColor else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = if (isSelected) ButtonDefaults.buttonElevation(defaultElevation = 4.dp) else null,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = name, style = MaterialTheme.typography.labelSmall)
    }
}
