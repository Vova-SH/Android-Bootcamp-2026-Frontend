package ru.sicampus.bootcamp2026.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle


val fontFamily = FontFamily(
    Font(R.font.commissionerflair_regular),
    Font(R.font.commissioner_light),
    Font(R.font.commissionerflair_thin),
    Font(R.font.commissionerflair_medium ),
    Font(R.font.commissionerflair_semibold)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)