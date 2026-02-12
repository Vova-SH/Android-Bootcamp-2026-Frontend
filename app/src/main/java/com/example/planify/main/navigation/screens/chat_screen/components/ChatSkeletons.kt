package com.example.planify.main.navigation.screens.chat_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.shimmer

@Composable
fun ChatSkeletons() {
    val shape = Locals.shapes.mediumShape

    val base = Locals.extras.foreground.copy(alpha = 0.08f)
    val highlight = Locals.extras.foreground.copy(alpha = 0.20f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.notificationCardHeight)
            .clip(shape)
            .shimmer(
                shape = shape,
                baseColor = base,
                highlightColor = highlight
            )
    )
}