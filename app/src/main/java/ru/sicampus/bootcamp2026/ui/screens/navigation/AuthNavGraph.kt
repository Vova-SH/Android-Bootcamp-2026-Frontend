package ru.sicampus.bootcamp2026.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.screens.auth.AuthorizationScreen
import ru.sicampus.bootcamp2026.ui.screens.profile.ProfileScreen

@Composable
fun AuthNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AuthNavigation.Auth
    ) {
        composable<AuthNavigation.Auth> {
            AuthorizationScreen(
                navController = navController
            )
        }

        composable<AuthNavigation.Profile> {
            ProfileScreen()
        }
    }
}