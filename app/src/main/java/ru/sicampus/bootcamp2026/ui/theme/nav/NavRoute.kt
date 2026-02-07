package ru.sicampus.bootcamp2026.ui.theme.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.theme.screens.AuthScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.ProfileScreen


@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AuthRoute
    ) {
        composable<AuthRoute> {
            AuthScreen(
                navController = navController
            )
        }

        composable<ListRoute> {
            ProfileScreen(
                navController = navController
            )
        }

    }
}