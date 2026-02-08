package ru.sicampus.bootcamp2026.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val BrandPrimary = Color(0xFF4F46E5)
val BrandDark = Color(0xFF3730A3)
val BrandLight = Color(0xFFC7D2FE)

val AccentPurple = Color(0xFF7C3AED)
val AccentPink = Color(0xFFDB2777)
val AccentTeal = Color(0xFF0D9488)

val BrandTertiary = Color(0xFFEF4444)

val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceBackground = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFF1F5F9)
val TextPrimary = Color(0xFF0F172A)
val TextSecondary = Color(0xFF64748B)
val TextTertiary = Color(0xFF94A3B8)
val OutlineLight = Color(0xFFE2E8F0)

val MainGradient = Brush.linearGradient(
    colors = listOf(BrandPrimary, AccentPurple)
)

val SecondaryGradient = Brush.linearGradient(
    colors = listOf(AccentPurple, AccentPink)
)