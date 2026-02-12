package com.example.planify.main.navigation.screens.main_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Bell
import com.adamglin.phosphoricons.bold.Gear
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.objectClickable
import com.example.planify.main.common.ui.withShapeBackground

@Composable
fun SettingsIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val gradient = Locals.gradients

    val shape = CircleShape

    Box(
        modifier = Modifier
            .shadow(
                elevation = Locals.dimens.elevation,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(Locals.icons.medium)
                    .objectClickable(
                        onClick = onClick
                    )
                    .withShapeBackground(
                        gradient = gradient.blue,
                        shape = shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(Locals.icons.smallPlus),
                    imageVector = PhosphorIcons.Bold.Gear,
                    contentDescription = null,
                    tint = colors.onPrimary
                )
            }
        }
    }
}
