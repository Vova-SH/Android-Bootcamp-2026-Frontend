package com.example.frontend.presentation.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val colors = lightColorScheme(
    primary = Purple,
    onPrimary = White,
    secondary = Purple,
    background = BgGray,
    surface = White,
    onSurface = Black,
)

@Composable
fun MeetupTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
