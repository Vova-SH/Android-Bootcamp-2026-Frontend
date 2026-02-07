package ru.sicampus.bootcamp2026.ui.theme

import androidx.compose.ui.graphics.Color

val PrimaryPurple = Color(0xFF7E57FF)
val PurplePrimary = Color(0xFF7E57FF)

val BackgroundColor = Color(0xFFFFFFFF)
val TextWhite = Color(0xFFFFFFFF)
val TextBlack = Color(0xFF000000)
val TextGray = Color(0xFF888888)
val BorderGray = Color(0xFFEBECEE)

val PurpleLight = Color(0xFFF3E8FF)
val BlueLight = Color(0xFFE0F2FE)
val GreenLight = Color(0xFFDCFCE7)
val RedLight = Color(0xFFFFE5E5)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun getMeetingColor(hex: String?): Color {
    if (hex.isNullOrBlank()) {
        return PurpleLight
    }
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        PurpleLight
    }
}