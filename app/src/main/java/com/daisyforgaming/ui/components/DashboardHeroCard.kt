package com.daisyforgaming.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.daisyforgaming.R
import com.daisyforgaming.ui.theme.ElectricCyan
import com.daisyforgaming.ui.theme.DarkSurface

@Composable
fun DashboardHeroCard(
    kernelVersion: String,
    currentGovernor: String,
    currentScheduler: String,
    thermalTemp: String,
    isThermalCritical: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkSurface, ElectricCyan.copy(alpha = 0.1f))
                )
            )
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "KERNEL VERSION",
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricCyan
                )
                Text(
                    text = kernelVersion,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoColumn(label = "GOVERNOR", value = currentGovernor, modifier = Modifier.weight(1f))
                    InfoColumn(label = "SCHEDULER", value = currentScheduler, modifier = Modifier.weight(1f))
                    InfoColumn(
                        label = "THERMAL",
                        value = thermalTemp,
                        modifier = Modifier.weight(1f),
                        valueColor = if (isThermalCritical) com.daisyforgaming.ui.theme.ErrorRed else ElectricCyan
                    )
                }
            }
            
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .clip(RoundedCornerShape(12.dp)),
                alpha = 0.8f
            )
        }
    }
}

@Composable
fun InfoColumn(
    label: String, 
    value: String, 
    modifier: Modifier = Modifier,
    valueColor: Color = ElectricCyan
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                slideInVertically { it } togetherWith slideOutVertically { -it }
            },
            label = "infoAnimation"
        ) { text ->
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                color = valueColor
            )
        }
    }
}
