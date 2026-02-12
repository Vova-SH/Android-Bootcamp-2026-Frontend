package com.example.frontend.presentation.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.frontend.data.auth.SessionManager
import com.example.frontend.data.api.ApiClient
import com.example.frontend.presentation.viewmodel.MeetupViewModel
import com.example.frontend.presentation.screens.meetuplist.MeetupListScreen
import com.example.frontend.presentation.screens.profile.ProfileScreen
import com.example.frontend.presentation.screens.createmeetup.CreateMeetupScreen
import com.example.frontend.presentation.screens.settings.SettingsScreen
import com.example.frontend.presentation.screens.notifications.NotificationsScreen
import com.example.frontend.presentation.screens.login.LoginScreen
import com.example.frontend.presentation.theme.MeetupTheme

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object MeetupList : Screen("meetup_list")
    object Profile : Screen("profile")
    object CreateMeetup : Screen("create_meetup")
    object Settings : Screen("settings")
    object Notifications : Screen("notifications")
}

@Composable
fun MeetupApp() {
    val nav = rememberNavController()
    val vm: MeetupViewModel = viewModel()
    val ctx = LocalContext.current
    val sm = remember { SessionManager(ctx) }

    // инициализирует апи клиент
    LaunchedEffect(Unit) {
        ApiClient.init(sm)
        vm.setSessionManager(sm)
    }

    val start = if (sm.isLoggedIn()) Screen.MeetupList.route else Screen.Login.route

    MeetupTheme {
        NavHost(navController = nav, startDestination = start) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = vm,
                    sm = sm,
                    onLoginSuccess = {
                        nav.navigate(Screen.MeetupList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = { }
                )
            }
            
            composable(Screen.MeetupList.route) {
                MeetupListScreen(
                    viewModel = vm,
                    onMeetupClick = { },
                    onAddClick = { nav.navigate(Screen.CreateMeetup.route) },
                    onProfileClick = { nav.navigate(Screen.Profile.route) },
                    onNotificationClick = { nav.navigate(Screen.Notifications.route) }
                )
            }
            
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onHomeClick = {
                        nav.navigate(Screen.MeetupList.route) {
                            popUpTo(Screen.MeetupList.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.CreateMeetup.route) {
                val uid = sm.getUserId()
                CreateMeetupScreen(
                    currentUserId = uid,
                    onMeetupCreated = { nav.popBackStack() }
                )
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onApply = { _, _, _ -> nav.popBackStack() },
                    onHomeClick = {
                        nav.navigate(Screen.MeetupList.route) {
                            popUpTo(Screen.MeetupList.route) { inclusive = true }
                        }
                    },
                    onProfileClick = { nav.navigate(Screen.Profile.route) },
                    onNotificationClick = { nav.navigate(Screen.Notifications.route) }
                )
            }
            
            composable(Screen.Notifications.route) {
                NotificationsScreen(
                    onHomeClick = {
                        nav.navigate(Screen.MeetupList.route) {
                            popUpTo(Screen.MeetupList.route) { inclusive = true }
                        }
                    },
                    onProfileClick = { nav.navigate(Screen.Profile.route) }
                )
            }
        }
    }
}
