package com.example.planify.main.navigation.screens.create_meeting_screen.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.CaretLeft
import com.adamglin.phosphoricons.regular.Star
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground

@Composable
fun StepIndicator(
    modifier: Modifier = Modifier,
    current: Int,
    total: Int
) {
    val gap = Locals.spacing.xs
    val dotSize = Locals.dimens.dotSize
    val pillWidth = Locals.dimens.pillWidth
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.outlineVariant

    if (total <= 0) return

    val transition = updateTransition(targetState = current, label = "step")

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { i ->
            val width = transition.animateDp(
                transitionSpec = { tween(260, easing = FastOutSlowInEasing) }
            ) { state -> if (state == i) pillWidth else dotSize }.value

            val color = transition.animateColor(
                transitionSpec = { tween(260, easing = FastOutSlowInEasing) }
            ) { state ->
                when {
                    state == i -> activeColor
                    i < state -> activeColor
                    else -> inactiveColor
                }
            }.value

            Box(
                modifier = Modifier
                    .width(pillWidth)
                    .height(dotSize),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(width)
                        .height(dotSize)
                        .background(
                            color = color,
                            shape = CircleShape
                        )
                )
            }

            if (i != total - 1) Spacer(Modifier.width(gap))
        }
    }
}

@Composable
fun TopBar(
    currentPage: Int,
    onBack: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.topBarHeight + Locals.dimens.createMeetingCompleteStatusBarHeight
            )
        ,
        color = colors.surface
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = Locals.spacing.m,
                        vertical = Locals.spacing.xs)
                    .height(Locals.dimens.topBarHeight - Locals.spacing.m -
                            WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .withShapeBackground(
                            shape = CircleShape,
                            color = colors.surface
                        )
                        .border(
                            color = Locals.extras.border,
                            shape = CircleShape,
                            width = 1.dp
                        )
                        .clickable(
                            onClick = onBack
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Locals.icons.medium)
                            .padding(Locals.spacing.xs),
                        imageVector = PhosphorIcons.Regular.CaretLeft,
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {

                    when(currentPage) {
                        0 -> {
                            Title(title = stringResource(R.string.title_info))
                            Spacer(modifier = Modifier.height(Locals.spacing.xxs))
                            Description(desc = stringResource(R.string.step1_of3))
                        }
                        1 -> {
                            Title(title = stringResource(R.string.title_time))
                            Spacer(modifier = Modifier.height(Locals.spacing.xxs))
                            Description(desc = stringResource(R.string.step2_of3))
                        }
                        2 -> {
                            Title(title = stringResource(R.string.title_participants))
                            Spacer(modifier = Modifier.height(Locals.spacing.xxs))
                            Description(desc = stringResource(R.string.step3_of3))
                        }
                    }
                }
            }

            Divider(color = Locals.extras.muted)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Locals.dimens.createMeetingCompleteStatusBarHeight),
                contentAlignment = Alignment.Center
            ) {
                StepIndicator(
                    current = currentPage,
                    total = 3
                )
            }
        }
    }
}

@Composable
fun Title(title: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = Locals.spacing.m),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displayLarge.copy(
                brush = Locals.gradients.blue
            )
        )
    }
}

@Composable
fun Description(desc: String) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Locals.spacing.m),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier
                .size(Locals.icons.smallPlus),
            imageVector = PhosphorIcons.Regular.Star,
            contentDescription = null,
            tint = colors.secondary
        )
        Spacer(modifier = Modifier.width(Locals.spacing.xxs))
        Text(
            text = desc,
            style = MaterialTheme.typography.bodyMedium,
            color = Locals.extras.mutedForeground.copy(alpha = 0.7f)
        )
    }
}

