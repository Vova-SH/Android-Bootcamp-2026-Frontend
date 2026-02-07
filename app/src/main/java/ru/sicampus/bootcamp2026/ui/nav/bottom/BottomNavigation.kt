package ru.sicampus.bootcamp2026.ui.nav.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.sicampus.bootcamp2026.ui.theme.BgGradientBottom
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun BottomNavigation (
    navController: NavController
) {
    val listItems = listOf(
        BottomNavigationItem.ScreenInbox,
        BottomNavigationItem.ScreenMeetings,
        BottomNavigationItem.ScreenProfile
    )
    Box(
        modifier =  Modifier
            .fillMaxWidth()
            .height(128.dp)
            .background(brush = BgGradientBottom)
    ) {
        Box(
            modifier = Modifier
                .padding(40.dp, 16.dp, 40.dp, 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(color = Black)
        ) {
            val backStackEntry by navController.currentBackStackEntryAsState()
            // val currentRoute = backStackEntry?.destination?.route
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listItems.forEach {
                    BottomNavigationButton(
                        it = it,
                        isSelected = backStackEntry?.destination?.hasRoute(it.route::class) ?: false,
                        onSelected = {
                            navController.navigate(it.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationButton(
    it: BottomNavigationItem,
    isSelected: Boolean,
    onSelected: () -> Unit
) {

    val contentColor = if (isSelected) Black else White

    Box(
        modifier = Modifier
            .size(72.dp)
            .clickable(
                onClick = onSelected,
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(White)
            )
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = ImageVector.vectorResource(it.iconId),
                it.title,
                tint = contentColor,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = it.title,
                fontSize = 10.sp,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}