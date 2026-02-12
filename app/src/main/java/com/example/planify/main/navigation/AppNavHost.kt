package com.example.planify.main.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.planify.core.ui.dialogs.AlertDialog
import com.example.planify.main.navigation.components.AuthRequiredDialog
import com.example.planify.main.navigation.screens.auth_screen.AuthScreen
import com.example.planify.main.navigation.screens.create_meeting_screen.ui.CreateMeeting
import com.example.planify.main.navigation.screens.init_screen.ui.InitScreen
import com.example.planify.main.navigation.screens.main_screen.MainScreenBox
import com.example.planify.main.navigation.screens.main_screen.views.profile.ui.edit_profile.EditProfileScreen
import com.example.planify.main.navigation.screens.meeting_info_screen.MeetingInfoScreen
import com.example.planify.main.navigation.screens.notifications_screen.NotificationsScreen
import com.example.planify.main.navigation.screens.registration.RegistrationScreen
import com.example.planify.main.navigation.screens.settings_screen.ui.SettingsScreen

@Composable
fun AppNavHost() {
    AppNavHost(
        viewModel = hiltViewModel()
    )
}

@Composable
private fun AppNavHost(
    viewModel: AppNavHostViewModel
) {
    val navController = rememberNavController()

    var dialog by remember { mutableStateOf<DialogType?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is NavHostUIEffect.Navigate -> {
                    Log.d("NavHostUIEffect", "Navigate: ${effect.route.route}")

                    val target = effect.route.route

                    navController.navigate(target) {
                        launchSingleTop = true
                        popUpTo(AppRoute.Init.route) { inclusive = true }
                    }
                }
                is NavHostUIEffect.ShowDialog ->{
                    dialog = effect.dialog
                    Log.d("NavHostUIEffect", "Dialog: ${effect.dialog}")
                }
            }
        }
    }

    dialog?.let { dialogInfo ->
        when (dialogInfo) {
            is DialogType.AuthError -> AuthRequiredDialog {
                navController.navigate(AppRoute.Auth.route) {
                    popUpTo(AppRoute.Main.route) { inclusive = true }
                    launchSingleTop = true
                }
                dialog = null }
            is DialogType.Generic -> AlertDialog(
                title = dialogInfo.title,
                message = dialogInfo.message,
                onCancel = { dialog = null },
                onDismiss = { dialog = null }
            )
        }
    }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = AppRoute.Init.route // <-----
    ) {
        composable(AppRoute.Init.route) {
            InitScreen(
                navHostController = navController
            )
        }
        composable(AppRoute.Main.route) {
            MainScreenBox(
                onSettings = { navController.navigate(AppRoute.Settings.route) },
                onCreateClick = { navController.navigate(AppRoute.CreateMeetingMenu.route) },
                onEditProfileClick = { navController.navigate(AppRoute.EditProfile.route) },
                navController = navController,
                onNotifications = { navController.navigate(AppRoute.Notifications.route) }
            )
        }
        composable(AppRoute.Auth.route) {
            AuthScreen(
                onRegister = { navController.navigate(AppRoute.Registration.route) },
                onForgetPassword = { },
                navHostController = navController
            )
        }
        composable(AppRoute.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(AppRoute.CreateMeetingMenu.route) {
            CreateMeeting(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(
            route = AppRoute.MeetingInfoMenu.PATTERN,
            arguments = listOf(navArgument(AppRoute.MeetingInfoMenu.ARG) {type = NavType.LongType} )
        ) {
            MeetingInfoScreen(
                onBack = {
                    navController.popBackStack(
                        AppRoute.Main.route,
                        inclusive = false
                    )
                }
            )
        }
        composable(AppRoute.EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
                onCameraClick = { }
            )
        }

        composable(AppRoute.Notifications.route) {
            NotificationsScreen(
                navController = navController
            )
        }

        composable(AppRoute.Registration.route) {
            RegistrationScreen(
                navHostController = navController
            )
        }
    }
}