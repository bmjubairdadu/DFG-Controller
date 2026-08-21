package com.dfgcontroller.ui.theme

import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF06070A)
val DarkSurface = Color(0xFF0F111A)
val DarkCard = Color(0xFF161924)

// Neon / Cyberpunk Colors
val ElectricCyan = Color(0xFF00FBFF)
val VulcanRed = Color(0xFFFF0033)
val ForestGreen = Color(0xFF00FF88)
val RoyalPurple = Color(0xFFBC00FF)
val GoldenOrange = Color(0xFFFFD600)
val HotPink = Color(0xFFFF00D4)

val ErrorRed = Color(0xFFFF3333)
val SuccessGreen = Color(0xFF00E676)

fun getThemeColor(name: String): Color {
    return when (name) {
        "Red" -> VulcanRed
        "Green" -> ForestGreen
        "Purple" -> RoyalPurple
        "Orange" -> GoldenOrange
        "Pink" -> HotPink
        else -> ElectricCyan
    }
}
