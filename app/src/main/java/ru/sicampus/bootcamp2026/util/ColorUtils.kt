package ru.sicampus.bootcamp2026.ui.utils

import androidx.compose.ui.graphics.Color

fun Color.darken(factor: Float): Color {
    return Color(
        red = (this.red * (1 - factor)).coerceAtLeast(0f),
        green = (this.green * (1 - factor)).coerceAtLeast(0f),
        blue = (this.blue * (1 - factor)).coerceAtLeast(0f),
        alpha = this.alpha
    )
}
