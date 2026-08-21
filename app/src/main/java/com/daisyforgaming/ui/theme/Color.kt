package com.daisyforgaming.ui.theme

import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF0A0C10)
val DarkSurface = Color(0xFF12141C)
val DarkCard = Color(0xFF1A1D26)

// Theme Colors
val ElectricCyan = Color(0xFF00E5FF)
val VulcanRed = Color(0xFFFF3D00)
val ForestGreen = Color(0xFF00E676)
val RoyalPurple = Color(0xFFD100FF)
val GoldenOrange = Color(0xFFFFAB00)

val ErrorRed = Color(0xFFFF5252)
val SuccessGreen = Color(0xFF00E676)

fun getThemeColor(name: String): Color {
    return when (name) {
        "Red" -> VulcanRed
        "Green" -> ForestGreen
        "Purple" -> RoyalPurple
        "Orange" -> GoldenOrange
        else -> ElectricCyan
    }
}
