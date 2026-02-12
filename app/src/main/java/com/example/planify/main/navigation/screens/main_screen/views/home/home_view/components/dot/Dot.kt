package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.dot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground

@Composable
fun Dot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .withShapeBackground(
                color = color,
                shape = CircleShape
            )
    )
}