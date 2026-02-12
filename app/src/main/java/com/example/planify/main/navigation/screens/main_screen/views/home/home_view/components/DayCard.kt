@file:Suppress("DEPRECATION")

package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.objectClickable
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.HomeViewModel
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.dot.Dot
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.dot.SkeletonDot
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.entities.CalendarDay
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayCard(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    isSelected: Boolean,
    onClick: () -> Unit,
    viewModel: HomeViewModel
) {
    val colors = MaterialTheme.colorScheme
    val extras = Locals.extras

    val backgroundColor = if (isSelected)
        extras.secondary else colors.background

    val borderColor = extras.border

    val contentColor = if (isSelected)
        colors.primary else colors.onSurface.copy(alpha = 0.9f)

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .clip(RectangleShape)
            .background(backgroundColor)
            .objectClickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RectangleShape,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")),
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = if (isSelected) MaterialTheme.typography.labelLarge
                else MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = contentColor,
                fontSize = 20.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (uiState.meetingsInfoShort) {
                is ResourceState.Loading, is ResourceState.Refreshing, is ResourceState.Idle -> {
                    repeat(3) {
                        SkeletonDot(
                            modifier = Modifier.padding(Locals.spacing.xxxxs)
                        )
                    }
                }

                is ResourceState.Success -> {
                    val state = uiState.meetingsInfoShort as ResourceState.Success
                    val dotCount = state.data[day.date] ?: 0

                    if (dotCount < 6) {
                        repeat(dotCount) {
                            Dot(
                                modifier = Modifier.padding(Locals.spacing.xxxxs),
                                color = colors.primary,
                                size = Locals.dimens.dotSize
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(Locals.dimens.dotContainerSize)
                                .clip(CircleShape)
                                .background(color = colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(Locals.spacing.xxxs),
                                text = dotCount.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onPrimary
                            )
                        }
                    }
                }

                is ResourceState.Error -> {
                    // TODO
                }
            }
        }
    }
}