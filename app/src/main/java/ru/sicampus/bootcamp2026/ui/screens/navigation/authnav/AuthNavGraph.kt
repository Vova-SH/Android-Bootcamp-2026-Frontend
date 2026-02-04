package ru.sicampus.bootcamp2026.ui.screens.navigation.authnav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.ui.screens.auth.AuthorizationScreen
import ru.sicampus.bootcamp2026.ui.screens.profile.ProfileScreen

@Composable
fun AuthNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    // UseCase и Launched
    val currentToken = runBlocking { AuthLocalDataSource.getToken() }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (currentToken == null) AuthNavigation.Auth else AuthNavigation.Profile
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