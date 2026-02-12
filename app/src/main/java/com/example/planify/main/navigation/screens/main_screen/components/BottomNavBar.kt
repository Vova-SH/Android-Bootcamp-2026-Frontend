package com.example.planify.main.navigation.screens.main_screen.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ChatCircle
import com.adamglin.phosphoricons.bold.EnvelopeSimple
import com.adamglin.phosphoricons.bold.HouseSimple
import com.adamglin.phosphoricons.bold.Plus
import com.adamglin.phosphoricons.bold.User
import com.adamglin.phosphoricons.regular.ChatCircle
import com.adamglin.phosphoricons.regular.EnvelopeSimple
import com.adamglin.phosphoricons.regular.HouseSimple
import com.adamglin.phosphoricons.regular.User
import com.example.planify.R
import com.example.planify.core.ui.pager_router_screen.PagerRouterNavigator
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.TextOnSurface
import com.example.planify.main.common.ui.objectClickable
import com.example.planify.main.common.ui.objectClickableNoAnimation
import com.example.planify.main.common.ui.withShapeBackground
import com.example.planify.main.navigation.screens.main_screen.MainScreenRoute

@Composable
private fun BottomNavItem(
    title: String,
    iconNormal: ImageVector,
    iconSelected: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val gradients = Locals.gradients
    val extras = Locals.extras

    val shape = Locals.shapes.largeShape

    val transition = updateTransition(targetState = selected, label = "navIndicator")

    val widthFraction by transition.animateFloat(label = "width") { isSelected ->
        if (isSelected) 0.55f else 0f
    }

    Column(
        modifier = Modifier
            .height(Locals.dimens.bottomBarHeight)
            .width(Locals.dimens.iconBottomBarWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(shape = Locals.shapes.mediumShape)
                .objectClickableNoAnimation(
                    onClick = onClick
                )
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .withShapeBackground(
                        color = Color.Transparent,
                        shape = Locals.shapes.mediumShape
                    )
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(Locals.icons.smallPlus),
                    imageVector = if (selected) iconSelected
                        else iconNormal,
                    contentDescription = null,
                    tint = if (selected) colors.primary else colors.onSurface
                )

                Spacer(modifier = Modifier.height(Locals.spacing.xxs))

                TextOnSurface(
                    text = title,
                    selected = selected
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(Locals.spacing.xxs)
                    .height(Locals.dimens.navIndicatorHeight)
                    .fillMaxWidth(widthFraction)
                    .clip(shape)
                    .shadow(
                        elevation = 18.dp,
                        shape = shape,
                        clip = false,
                        ambientColor = extras.primaryGlow,
                        spotColor = extras.primaryGlow
                    )
                    .background(gradients.blue)
            )
        }
    }
}

@Composable
fun FloatingActionItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val gradient = Locals.gradients
    val shape = CircleShape

    Box(
        modifier = modifier
            .shadow(
                elevation = Locals.dimens.elevation,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(shape = shape)
                .objectClickable(
                    onClick = onClick
                )
        ) {
            Box(
                modifier = Modifier
                    .withShapeBackground(
                        gradient = gradient.blue,
                        shape = shape
                    )
                    .size(Locals.dimens.floatingActionButtonRadius),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(Locals.icons.medium),
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.onPrimary
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    pagerRouter: PagerRouterNavigator,
    onOpenCreateDialog: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.bottomBarHeight),
        color = colors.surface
    ) {
       Row(
           modifier = Modifier
               .navigationBarsPadding(),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.SpaceAround
       ) {

           BottomNavItem(
               title = stringResource(R.string.home),
               iconNormal = PhosphorIcons.Regular.HouseSimple,
               iconSelected = PhosphorIcons.Bold.HouseSimple,
               selected = pagerRouter.currentRoute == MainScreenRoute.Home,
               onClick = {
                   pagerRouter.navigateTo(MainScreenRoute.Home)
               }
           )

           BottomNavItem(
               title = stringResource(R.string.chats),
               iconNormal = PhosphorIcons.Regular.ChatCircle,
               iconSelected = PhosphorIcons.Bold.ChatCircle,
               selected = pagerRouter.currentRoute == MainScreenRoute.Chat,
               onClick = {
                   pagerRouter.navigateTo(MainScreenRoute.Chat)
               }
           )

           Spacer(modifier = Modifier.width(Locals.spacing.xxl))

           BottomNavItem(
               title = stringResource(R.string.inbox),
               iconNormal = PhosphorIcons.Regular.EnvelopeSimple,
               iconSelected = PhosphorIcons.Bold.EnvelopeSimple,
               selected = pagerRouter.currentRoute == MainScreenRoute.Inbox,
               onClick = {
                   pagerRouter.navigateTo(MainScreenRoute.Inbox)
               }
           )

           BottomNavItem(
               title = stringResource(R.string.profile),
               iconNormal = PhosphorIcons.Regular.User,
               iconSelected = PhosphorIcons.Bold.User,
               selected = pagerRouter.currentRoute == MainScreenRoute.Profile,
               onClick = {
                   pagerRouter.navigateTo(MainScreenRoute.Profile)
               }
           )
       }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        FloatingActionItem(
            modifier = Modifier
                .offset(y = (-20).dp)
                .align(Alignment.Center),
            icon = PhosphorIcons.Bold.Plus,
            onClick = onOpenCreateDialog
        )
    }
}