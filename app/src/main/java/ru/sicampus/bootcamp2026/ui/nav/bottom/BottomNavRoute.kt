package ru.sicampus.bootcamp2026.ui.nav.bottom

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.sicampus.bootcamp2026.ui.nav.AppRoute
import ru.sicampus.bootcamp2026.ui.screen.inbox.InboxScreen
import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingsScreen
import ru.sicampus.bootcamp2026.ui.screen.profile.Profile

@Composable
fun NavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = AppRoute.MeetingsRoute
    ) {
        composable<AppRoute.InboxRoute> {
            InboxScreen()
        }
        composable<AppRoute.MeetingsRoute> {
            MeetingsScreen()
        }
        composable<AppRoute.ProfileRoute> {
            Profile()
        }
    }
}