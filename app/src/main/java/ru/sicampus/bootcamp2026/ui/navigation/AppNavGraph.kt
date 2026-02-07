package ru.sicampus.bootcamp2026.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.auth.LoginScreen
import ru.sicampus.bootcamp2026.ui.auth.LoginViewModel
import ru.sicampus.bootcamp2026.ui.auth.RegisterScreen
import ru.sicampus.bootcamp2026.ui.auth.RegisterViewModel
import ru.sicampus.bootcamp2026.ui.main.MainScreen
import ru.sicampus.bootcamp2026.ui.meeting.CreateMeetingScreen
import ru.sicampus.bootcamp2026.ui.meeting.MeetingDetailScreen
import ru.sicampus.bootcamp2026.ui.profile.ProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    // Общий фон для всех экранов
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.green_gradient),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            // Экран входа
            composable(Screen.Login.route) {
                val viewModel: LoginViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                // Следим за успешным входом и переходим на MainScreen
                LaunchedEffect(uiState.isSuccess) {
                    if (uiState.isSuccess) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }

                LoginScreen(
                    state = uiState,
                    onEmailChange = { email ->
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.LoginUiEvent.EmailChanged(email))
                    },
                    onPasswordChange = { password ->
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.LoginUiEvent.PasswordChanged(password))
                    },
                    onTogglePasswordVisibility = {
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.LoginUiEvent.TogglePasswordVisibility)
                    },
                    onLoginClick = {
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.LoginUiEvent.Login)
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            // Экран регистрации
            composable(Screen.Register.route) {
                val viewModel: RegisterViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                // Следим за успешной регистрацией и переходим на MainScreen
                LaunchedEffect(uiState.isSuccess) {
                    if (uiState.isSuccess) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                }

                RegisterScreen(
                    state = uiState,
                    onUsernameChange = { username ->
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.RegisterUiEvent.UsernameChanged(username))
                    },
                    onEmailChange = { email ->
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.RegisterUiEvent.EmailChanged(email))
                    },
                    onPasswordChange = { password ->
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.RegisterUiEvent.PasswordChanged(password))
                    },
                    onConfirmPasswordChange = { password ->
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.RegisterUiEvent.ConfirmPasswordChanged(password))
                    },
                    onTogglePasswordVisibility = {
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.RegisterUiEvent.TogglePasswordVisibility)
                    },
                    onToggleConfirmPasswordVisibility = {
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.RegisterUiEvent.ToggleConfirmPasswordVisibility)
                    },
                    onLoginClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        viewModel.onEvent(ru.sicampus.bootcamp2026.ui.auth.RegisterUiEvent.Register)
                    }
                )
            }

            // Главный экран с табами (Home, Calendar, Notifications) с нижней панелью навигации
            composable(Screen.Main.route) {
                MainScreen(
                    onNavigateToCreate = {
                        navController.navigate(Screen.CreateMeeting.route)
                    },
                    onNavigateToDetails = { meetingId ->
                        navController.navigate(Screen.MeetingDetail.createRoute(meetingId))
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }

            // Экран деталей встречи
            composable(
                route = Screen.MeetingDetail.route,
                arguments = listOf(
                    navArgument("meetingId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val meetingId = backStackEntry.arguments?.getString("meetingId") ?: ""
                MeetingDetailScreen(
                    meetingId = meetingId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Экран создания встречи
            composable(Screen.CreateMeeting.route) {
                CreateMeetingScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Экран профиля
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToLogin = {
                        // Очищаем весь стек навигации и переходим на экран логина
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

