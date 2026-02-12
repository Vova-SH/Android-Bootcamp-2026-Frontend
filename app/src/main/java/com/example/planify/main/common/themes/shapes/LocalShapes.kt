package com.example.planify.main.common.themes.shapes

import androidx.compose.runtime.staticCompositionLocalOf

val LocalShapes = staticCompositionLocalOf<Shapes> {
    error("Shapes clip not provided")
}