package ru.sicampus.bootcamp2026.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.sicampus.bootcamp2026.ui.screens.auth.AuthorizationScreen
import ru.sicampus.bootcamp2026.ui.screens.createinvite.CreateInviteScreen
import ru.sicampus.bootcamp2026.ui.screens.invites.InvitesScreen
import ru.sicampus.bootcamp2026.ui.screens.profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.screens.schedule.ScheduleScreen

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