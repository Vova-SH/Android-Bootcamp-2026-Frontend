package ru.sicampus.bootcamp2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.screens.Login.LoginScreen
import ru.sicampus.bootcamp2026.screens.Register.RegisterScreen
import ru.sicampus.bootcamp2026.ui.theme.screen.ListScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    var startDestination by remember { mutableStateOf("auth") }

    LaunchedEffect(Unit) {
        val token = AuthLocalDataSource.getTokenSuspend()
        startDestination = if (token != null) "list" else "auth"
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("auth") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("list") {
            ListScreen(navController = navController)
        }
    }
}

