package com.example.planify.main.navigation.screens.inbox_screen.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planify.R
import com.example.planify.core.ui.pager_router_screen.PagerRouterScreen
import com.example.planify.core.ui.pager_router_screen.PagerRouterState
import com.example.planify.core.ui.pager_router_screen.rememberPagerRouterScreenState
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground
import com.example.planify.main.navigation.screens.inbox_screen.InboxRoute
import com.example.planify.main.navigation.screens.inbox_screen.InboxViewModel
import com.example.planify.main.navigation.screens.inbox_screen.ui.views.InboxViewIncoming
import com.example.planify.main.navigation.screens.inbox_screen.ui.views.InboxViewSent

@Composable
fun InboxView(
    scaffoldPadding: PaddingValues
) {
    InboxView(
        viewModel = hiltViewModel(),
        scaffoldPadding = scaffoldPadding
    )
}

@Composable
private fun InboxView(
    viewModel: InboxViewModel,
    scaffoldPadding: PaddingValues
) {
    val pagerState = rememberPagerRouterScreenState(
        routes = InboxRoute.routes,
        startRoute = InboxRoute.Incoming
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PagerRouterScreen(
                modifier = Modifier.fillMaxSize(),
                state = pagerState
            ) {
                screen(InboxRoute.Incoming) {
                    InboxViewIncoming(viewModel)
                }
                screen(InboxRoute.Sent) {
                    InboxViewSent(viewModel)
                }
            }
        }

        TopNavButtonIsland(
            pagerState = pagerState
        )

    }

}

@Composable
fun TopNavButtonIsland(
    pagerState: PagerRouterState
) {
    val colors = MaterialTheme.colorScheme

    val backgroundIncoming by animateColorAsState(
        targetValue = if (pagerState.currentRoute == InboxRoute.Incoming) colors.primaryContainer.copy(alpha = 0.1f) else Color.Transparent,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )

    val textIncoming by animateColorAsState(
        targetValue = if (pagerState.currentRoute == InboxRoute.Incoming) colors.primary else colors.onSurface,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )

    val backgroundSent by animateColorAsState(
        targetValue = if (pagerState.currentRoute == InboxRoute.Sent) colors.primaryContainer.copy(alpha = 0.1f) else Color.Transparent,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )

    val textSent by animateColorAsState(
        targetValue = if (pagerState.currentRoute == InboxRoute.Sent) colors.primary else colors.onSurface,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.inboxBoxTopNavBarHeight)
            .background(color = Color.Transparent)
    ) {
        Card(
            modifier = Modifier
                .padding(
                    horizontal = Locals.spacing.m,
                    vertical = Locals.spacing.s
                )
                .shadow(
                    elevation = 6.dp,
                    shape = Locals.shapes.smallShape,
                    ambientColor = Locals.extras.mutedForeground.copy(0.3f),
                    spotColor = Locals.extras.mutedForeground.copy(0.3f)
                )
                .fillMaxSize(),
            shape = Locals.shapes.smallShape,
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Locals.spacing.xxs)
                        .withShapeBackground(
                            color = backgroundIncoming,
                            shape = Locals.shapes.smallShape
                        )
                        .fillMaxHeight()
                        .clickable {
                            pagerState.navigateTo(InboxRoute.Incoming)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.incoming),
                        style = MaterialTheme.typography.bodySmall,
                        color = textIncoming
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Locals.spacing.xxs)
                        .withShapeBackground(
                            color = backgroundSent,
                            shape = Locals.shapes.smallShape
                        )
                        .fillMaxHeight()
                        .clickable {
                            pagerState.navigateTo(InboxRoute.Sent)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.sent),
                        style = MaterialTheme.typography.bodySmall,
                        color = textSent
                    )
                }
            }
        }
    }
}