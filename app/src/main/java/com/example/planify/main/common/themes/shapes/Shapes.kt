package com.example.planify.main.common.themes.shapes

import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.planify.main.common.themes.dimens.Dimens

data class Shapes(
    val bottomNavBarShape: Shape,
    val smallShape: Shape,
    val mediumShape: Shape,
    val largeShape: Shape
)

fun shapes(dimens: Dimens) = Shapes(
    bottomNavBarShape = RoundedCornerShape(
        topStart = dimens.cornerRadius * 2,
        topEnd = dimens.cornerRadius * 2
    ),
    smallShape = RoundedCornerShape(dimens.cornerRadius / 2),
    mediumShape = RoundedCornerShape(dimens.cornerRadius),
    largeShape = RoundedCornerShape(dimens.cornerRadius * 2.5f)
)

