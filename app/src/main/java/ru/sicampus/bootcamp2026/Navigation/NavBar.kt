package ru.sicampus.bootcamp2026.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun NavBar(
    navController: NavHostController
) {
    val currentDestination = navController.currentDestination
    val selectedTab = when (currentDestination?.route) {
        Routes.Invitations.route -> 0
        Routes.Home.route -> 1
        Routes.Schedule.route -> 2
        Routes.Profile.route -> 3
        else -> 1
    }

    val colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        indicatorColor = MaterialTheme.colorScheme.primary
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = {
                if (selectedTab != 0) {
                    navController.navigate(Routes.Invitations.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text("Приглашения", fontSize = 12.sp)
            },
            colors = colors
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = {
                if (selectedTab != 1) {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text("Главная", fontSize = 12.sp)
            },
            colors = colors
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = {
                if (selectedTab != 2) {
                    navController.navigate(Routes.Schedule.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text("Расписание", fontSize = 12.sp)
            },
            colors = colors
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = {
                if (selectedTab != 3) {
                    navController.navigate(Routes.Profile.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = {
                Text("Профиль", fontSize = 12.sp)
            },
            colors = colors
        )
    }
}
