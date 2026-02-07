package ru.sicampus.bootcamp2026.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.theme.BorderGray

fun Modifier.customDashedBorder(
    color: Color = BorderGray,
    strokeWidth: Dp = 3.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 3.dp,
    cornerRadius: Dp = 12.dp
): Modifier = this.drawBehind {

    val strokeWidthPx = strokeWidth.toPx()
    val dashWidthPx = dashLength.toPx()
    val gapWidthPx = gapLength.toPx()
    val cornerRadiusPx = cornerRadius.toPx()

    drawRoundRect(
        color = color,
        size = size,
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
        style = Stroke(
            width = strokeWidthPx,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashWidthPx, gapWidthPx),
                0f
            )
        )
    )
}