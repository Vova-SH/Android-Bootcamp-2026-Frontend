package ru.sicampus.bootcamp2026.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.screens.navigation.authnav.AuthNavGraph
import ru.sicampus.bootcamp2026.ui.screens.navigation.BottomNavigation
import ru.sicampus.bootcamp2026.ui.screens.navigation.NavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val isBottomBar = currentRoute in listOf(
        "ProfileScreen",
        "InvitesScreen",
        "CreateInviteScreen",
        "ScheduleScreen"
    )
    if (isBottomBar) {
        Scaffold(
            bottomBar = {
                BottomNavigation(navController = navController)
            }
        ) {
            NavGraph(navHostController = navController)
        }
    } else {
        Scaffold {
            AuthNavGraph()
        }
    }

}