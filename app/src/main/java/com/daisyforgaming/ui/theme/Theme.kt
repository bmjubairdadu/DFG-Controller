package com.daisyforgaming.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = DarkBackground,
    primaryContainer = ElectricCyanGlow,
    onPrimaryContainer = ElectricCyan,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF222733),
    onSurfaceVariant = Color(0xFFB0BEC5),
    error = ErrorRed,
    onError = DarkBackground
)

@Composable
fun DFGControllerTheme(
    accentColor: Color = ElectricCyan,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = accentColor,
        onPrimary = DarkBackground,
        primaryContainer = accentColor.copy(alpha = 0.1f),
        onPrimaryContainer = accentColor,
        background = DarkBackground,
        onBackground = Color.White,
        surface = DarkSurface,
        onSurface = Color.White,
        surfaceVariant = Color(0xFF222733),
        onSurfaceVariant = Color(0xFFB0BEC5),
        error = ErrorRed,
        onError = DarkBackground
    )
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
