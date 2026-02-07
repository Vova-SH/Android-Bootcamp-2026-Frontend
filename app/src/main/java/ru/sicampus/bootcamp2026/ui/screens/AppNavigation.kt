package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.data.model.AuthViewModel

private object Routes {
    const val AUTH = "auth"
    const val MAIN = "main_screen"
    const val NEW_MEETING = "new_meeting_screen"
    const val INVITES = "invites_screen"
    const val PROFILE = "profile_screen"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authVm: AuthViewModel = viewModel()
    val state by authVm.state.collectAsState()

    LaunchedEffect(state.isAuthed) {
        if (state.isAuthed) {
            navController.navigate(Routes.MAIN) {
                popUpTo(Routes.AUTH) { inclusive = true }
            }
        } else {
            if (navController.currentDestination?.route != Routes.AUTH) {
                navController.navigate(Routes.AUTH) {
                    popUpTo(Routes.MAIN) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (state.isAuthed) Routes.MAIN else Routes.AUTH
    ) {

        composable(Routes.AUTH) {
            AuthScreen(
                viewModel = authVm,
                state = state,
                onRegister = { _, _, _ -> },
                onLogin = { _, _ -> }
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                onAddMeetingClicked = { navController.navigate(Routes.NEW_MEETING) },
                onInvitesClicked = { navController.navigate(Routes.INVITES) },
                onProfileClicked = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(Routes.NEW_MEETING) {
            NewMeetingScreen(onBackClicked = { navController.popBackStack() })
        }

        composable(Routes.INVITES) {
            InvitesScreen(onBackClicked = { navController.popBackStack() })
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBackClicked = { navController.popBackStack() },
                onLogoutClicked = { authVm.logout() }
            )
        }
    }
}