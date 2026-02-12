package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.dot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.shimmer

@Composable
fun SkeletonDot(
    modifier: Modifier = Modifier
) {
    val base = Locals.extras.foreground.copy(alpha = 0.08f)
    val highlight = Locals.extras.foreground.copy(alpha = 0.20f)

    Box(
        modifier = modifier
            .size(Locals.dimens.dotSize)
            .clip(CircleShape)
            .shimmer(
                shape = CircleShape,
                baseColor = base,
                highlightColor = highlight
            )
    )
}