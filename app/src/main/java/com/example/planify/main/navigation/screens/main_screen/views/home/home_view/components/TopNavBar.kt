package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.planify.R
import com.example.planify.core.ui.pager_router_screen.PagerRouterNavigator
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.TopNavBarItemText
import com.example.planify.main.common.ui.objectClickable
import com.example.planify.main.common.ui.withShapeBackground
import com.example.planify.main.navigation.screens.main_screen.views.home.home_view.HomeViewRoute

@Composable
fun TopNavBarItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val extras = Locals.extras
    val shape = RectangleShape

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary
        else Color.Transparent,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            extras.secondary else colors.surface,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    Column(
        modifier = modifier
            .height(Locals.dimens.topNavBarHeight),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .objectClickable(
                    onClick = onClick
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape
                )
                .fillMaxSize()
                .withShapeBackground(
                    shape = shape,
                    color = backgroundColor
                ),
            contentAlignment = Alignment.Center
        ) {
            TopNavBarItemText(
                modifier = Modifier,
                text = title,
                isSelected = isSelected
            )
        }
    }
}

@Composable
fun TopNavBar(
    pagerRouter: PagerRouterNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TopNavBarItem(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(R.string.day_schedule),
                isSelected = pagerRouter.currentRoute == HomeViewRoute.Day
            ) {
                pagerRouter.navigateTo(HomeViewRoute.Day)
            }
            TopNavBarItem(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(R.string.week_schedule),
                isSelected = pagerRouter.currentRoute == HomeViewRoute.Week
            ) {
                pagerRouter.navigateTo(HomeViewRoute.Week)
            }
            TopNavBarItem(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(R.string.month_schedule),
                isSelected = pagerRouter.currentRoute == HomeViewRoute.Month
            ) {
                pagerRouter.navigateTo(HomeViewRoute.Month)
            }
        }
    }
}