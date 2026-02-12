package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.entities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.shimmer

@Composable
fun SkeletonMeetingCard(
    modifier: Modifier = Modifier
) {
    val shape = Locals.shapes.mediumShape

    val base = Locals.extras.foreground.copy(alpha = 0.04f)
    val highlight = Locals.extras.foreground.copy(alpha = 0.10f)

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
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .shimmer(
                    shape = shape,
                    baseColor = base,
                    highlightColor = highlight
                )
        )
    }
}
