package ru.sicampus.bootcamp2026.presentation.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.presentation.ui.screens.main.invites.InvitesScreen
import ru.sicampus.bootcamp2026.presentation.ui.screens.main.meets.MeetsScreen
import ru.sicampus.bootcamp2026.presentation.ui.screens.main.profile.ProfileInfoScreen
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun MenuScreen(nav: NavHostController) {
    val innerNav = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(innerNav) }
    ) { paddingValues ->
        NavHost(
            navController = innerNav,
            startDestination = "meets",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("meets") { MeetsScreen() }
            composable("profile") { ProfileInfoScreen() }
            composable("invites") { InvitesScreen() }
        }
    }
}

@Composable
fun BottomNavBar(innerNav: NavHostController) {
    val backStack by innerNav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        fun go(route: String) = innerNav.navigate(route) {
            popUpTo(innerNav.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        NavigationBarItem(
            selected = route == "invites",
            onClick = { go("invites") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Приглашения"
                )
            },
            label = { Text("Приглашения") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        NavigationBarItem(
            selected = route == "meets",
            onClick = { go("meets") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Встречи"
                )
            },
            label = { Text("Встречи") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        NavigationBarItem(
            selected = route == "profile",
            onClick = { go("profile") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Профиль"
                )
            },
            label = { Text("Профиль") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    AndroidBootcamp2026FrontendTheme(darkTheme = false) {
        MenuScreen(rememberNavController())
    }
}