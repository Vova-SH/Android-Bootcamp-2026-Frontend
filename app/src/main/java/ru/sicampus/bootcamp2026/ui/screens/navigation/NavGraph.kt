package ru.sicampus.bootcamp2026.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.sicampus.bootcamp2026.ui.screens.CreateInviteScreen
import ru.sicampus.bootcamp2026.ui.screens.InvitesScreen
import ru.sicampus.bootcamp2026.ui.screens.profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.screens.ScheduleScreen

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "ProfileScreen") {

        composable("ProfileScreen") {
            ProfileScreen()
        }
        composable("InvitesScreen") {
            InvitesScreen()
        }
        composable("CreateInviteScreen") {
            CreateInviteScreen()
        }
        composable("ScheduleScreen") {
            ScheduleScreen()
        }
    }
}