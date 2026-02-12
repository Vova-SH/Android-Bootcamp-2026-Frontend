package com.example.planify.main.common.themes.icons

import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.example.planify.main.common.themes.dimens.Dimens

data class Icons(
    val small: Dp,
    val smallUp: Dp,
    val smallPlus: Dp,
    val mediumLower: Dp,
    val medium: Dp,
    val mediumPlus: Dp,
    val largeLower: Dp,
    val large: Dp,
    val largePlus: Dp
)

fun defaultIcons(dimens: Dimens) = Icons(
    small = dimens.iconSize * 2,
    smallUp = dimens.iconSize * 3,
    smallPlus = dimens.iconSize * 4,
    mediumLower = dimens.iconSize * 6,
    medium = dimens.iconSize * 8,
    mediumPlus = dimens.iconSize * 10,
    largeLower = dimens.iconSize * 12,
    large = dimens.iconSize * 16,
    largePlus = dimens.iconSize * 20
)