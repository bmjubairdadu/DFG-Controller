package com.daisyforgaming.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun GlowSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..255f,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    var isDragging by remember { mutableStateOf(false) }
    val thumbScale by animateFloatAsState(if (isDragging) 1.5f else 1.0f, label = "thumbScale")
    
    val animatedAccentColor by animateColorAsState(targetValue = accentColor, label = "sliderAccent")

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val width = constraints.maxWidth.toFloat()
        val range = valueRange.endInclusive - valueRange.start
        val position = ((value - valueRange.start) / range) * width

        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.DarkGray.copy(alpha = 0.5f))
        )
        
        // Active Track (drawn on canvas for simplicity)
        Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures(onPress = { offset ->
                val newValue = (offset.x / width) * range + valueRange.start
                onValueChange(newValue.coerceIn(valueRange))
            })
        }.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { isDragging = true },
                onDragEnd = { isDragging = false },
                onDragCancel = { isDragging = false },
                onDrag = { change, _ ->
                    val newValue = (change.position.x / width) * range + valueRange.start
                    onValueChange(newValue.coerceIn(valueRange))
                }
            )
        }) {
            // Draw active track
            drawLine(
                color = animatedAccentColor,
                start = androidx.compose.ui.geometry.Offset(0f, size.height / 2),
                end = androidx.compose.ui.geometry.Offset(position, size.height / 2),
                strokeWidth = 4.dp.toPx()
            )

            // Draw thumb glow
            drawCircle(
                color = animatedAccentColor.copy(alpha = 0.2f),
                radius = (12.dp * thumbScale).toPx(),
                center = androidx.compose.ui.geometry.Offset(position, size.height / 2)
            )
            // Draw thumb
            drawCircle(
                color = animatedAccentColor,
                radius = (8.dp * thumbScale).toPx(),
                center = androidx.compose.ui.geometry.Offset(position, size.height / 2)
            )
        }
    }
}
