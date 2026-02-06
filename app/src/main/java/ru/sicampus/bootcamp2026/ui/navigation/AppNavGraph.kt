package ru.sicampus.bootcamp2026.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.sicampus.bootcamp2026.R
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
            startDestination = Screen.Main.route
        ) {
            // Главный экран с табами (Home, Calendar, Notifications)
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

