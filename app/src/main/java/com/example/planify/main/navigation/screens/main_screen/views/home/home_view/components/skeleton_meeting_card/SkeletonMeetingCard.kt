package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.skeleton_meeting_card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.shimmer

@Composable
fun SkeletonMeetingCard(
    modifier: Modifier = Modifier
) {
    val shape = Locals.shapes.mediumShape

    val base = Locals.extras.foreground.copy(alpha = 0.08f)
    val highlight = Locals.extras.foreground.copy(alpha = 0.20f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.meetingCardHeight)
            .padding(
                horizontal = Locals.spacing.m
            )
            .clip(shape)
            .shimmer(
                shape = shape,
                baseColor = base,
                highlightColor = highlight
            )
    )
}
