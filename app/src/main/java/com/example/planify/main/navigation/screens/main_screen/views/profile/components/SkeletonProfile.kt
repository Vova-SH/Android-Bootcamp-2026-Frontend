package com.example.planify.main.navigation.screens.main_screen.views.profile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.shimmer

@Composable
fun SkeletonProfile(
    modifier: Modifier = Modifier
) {
    val shape = Locals.shapes.mediumShape

    val base = Locals.extras.foreground.copy(alpha = 0.08f)
    val highlight = Locals.extras.foreground.copy(alpha = 0.20f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Locals.dimens.profileCardHeight1)
            .shimmer(
                shape = shape,
                baseColor = base,
                highlightColor = highlight
            )
            .clip(shape)
    )

    Spacer(Modifier.height(Locals.spacing.m))

    Box(
        modifier = modifier
            .wrapContentSize()
            .shimmer(
                shape = shape,
                baseColor = base,
                highlightColor = highlight
            )
            .clip(Locals.shapes.smallShape)
    ) {
        Text(
            text = stringResource(R.string.contacts),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Transparent
        )
    }

    Spacer(Modifier.height(Locals.spacing.xs))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Locals.dimens.profileCardHeight2)
            .shimmer(
                shape = shape,
                baseColor = base,
                highlightColor = highlight
            )
            .clip(shape)
    )

    Spacer(Modifier.height(Locals.spacing.m))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Locals.dimens.logOutButtonHeight)
            .shimmer(
                shape = shape,
                baseColor = base,
                highlightColor = highlight
            )
            .clip(shape)
    )
}
