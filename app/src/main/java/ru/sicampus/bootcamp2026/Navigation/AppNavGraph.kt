package ru.sicampus.bootcamp2026.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthScreen
import ru.sicampus.bootcamp2026.ui.screen.registration.RegistrationScreen
import ru.sicampus.bootcamp2026.ui.screen.profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.screen.main.MainScreen
import ru.sicampus.bootcamp2026.ui.screen.inviting.InvitingScreen
import ru.sicampus.bootcamp2026.ui.screen.schedule.ScheduleScreen

import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.Auth.route,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
        composable(Routes.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(Routes.Registration.route) {
            RegistrationScreen(navController = navController)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Routes.Home.route) {
            MainScreen(navController = navController)
        }
        composable(Routes.Invitations.route) {
            InvitingScreen(navController = navController)
        }
        composable(Routes.Schedule.route) {
            ScheduleScreen(navController = navController)
        }
        }
    }
}
