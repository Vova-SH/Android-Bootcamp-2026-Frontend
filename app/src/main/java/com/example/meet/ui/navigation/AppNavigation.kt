package com.example.meet.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.meet.Screen
import com.example.meet.ui.screens.auth.LoginScreen
import com.example.meet.ui.screens.auth.RegisterScreen
import com.example.meet.ui.screens.list.InvitationsListScreen
import com.example.meet.ui.screens.main.MainMeetScreen
import com.example.meet.ui.screens.main.NotificationsScreen
import com.example.meet.ui.screens.main.ScheduleScreen
import com.example.meet.ui.screens.main.SplashScreen
import com.example.meet.ui.screens.meetings.CreateMeetingScreen
import com.example.meet.ui.screens.meetings.MeetingDetailsScreen
import com.example.meet.ui.screens.list.InvitationDetailsScreen
import com.example.meet.ui.screens.profile.InfoProfileScreen
import com.example.meet.ui.screens.profile.ProfileScreen
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.MainMeet.route) {
            MainMeetScreen(navController = navController)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.InfoProfile.route) {
            InfoProfileScreen(navController = navController)
        }
        composable(Screen.Schedule.route) {
            ScheduleScreen(navController = navController)
        }
        composable(Screen.InvitationsList.route) {
            InvitationsListScreen(navController = navController)
        }
        composable(Screen.CreateMeeting.route) {
            CreateMeetingScreen(navController = navController)
        }
        composable(
            route = Screen.MeetingDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if (id != null) {
                MeetingDetailsScreen(navController = navController, meetingId = id)
            }
        }
        composable(
            route = Screen.InvitationDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if (id != null) {
                InvitationDetailsScreen(navController = navController, invitationId = id)
            }
        }
    }
}
