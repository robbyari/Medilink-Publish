package com.robbyari.monitoring.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = lightColorScheme(
    primary = ButtonColor,
    secondary = ButtonBackground,
    tertiary = Orange,
)

private val LightColorScheme = lightColorScheme(
    primary = ButtonColor,
    secondary = ButtonBackground,
    tertiary = Orange,
)

@Composable
fun MonitoringTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}