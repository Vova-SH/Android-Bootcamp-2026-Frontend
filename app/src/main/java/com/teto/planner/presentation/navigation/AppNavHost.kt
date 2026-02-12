package com.teto.planner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.teto.planner.presentation.features.auth.login.LoginScreen
import com.teto.planner.presentation.features.auth.register.RegisterScreen
import com.teto.planner.presentation.features.invitations.InvitationScreen
import com.teto.planner.presentation.features.meeting_create.MeetingCreateScreen
import com.teto.planner.presentation.features.profile.ProfileScreen
import com.teto.planner.presentation.features.schedule.ScheduleScreen
import com.teto.planner.presentation.features.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToSchedule = {
                    navController.navigate(Screen.Schedule.route) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = hiltViewModel(),
                onLoginSuccess = {
                    navController.navigate(Screen.Schedule.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = hiltViewModel(),
                onRegisterSuccess = {
                    navController.navigate(Screen.Schedule.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        navigation(
            startDestination = Screen.Schedule.route,
            route = "main_graph"
        ) {
            composable(Screen.Schedule.route) {
                ScheduleScreen(
                    onCreateMeeting = { navController.navigate(Screen.MeetingCreate.route) },
                    onProfileClick = { navController.navigate(Screen.Profile.route) },
                    viewModel = hiltViewModel()
                )
            }

            composable(Screen.Invitations.route) {
                InvitationScreen(
                    viewModel = hiltViewModel(),
                    onProfileClick = { navController.navigate(Screen.Profile.route) },
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onSave = { navController.popBackStack() },
                    onEdit = {},
                    onExit = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    viewModel = hiltViewModel()
                )
            }

            composable(Screen.MeetingCreate.route) {
                MeetingCreateScreen(
                    viewModel = hiltViewModel(),
                    onBack = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() }
                )
            }
        }
    }
}