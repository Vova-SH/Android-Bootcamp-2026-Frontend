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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.sicampus.bootcamp2026.ui.theme.screen.auth.ru.sicampus.bootcamp2026.ui.theme.screen.list.ListScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        val token = AuthLocalDataSource.getTokenSuspend()
        startDestination = if (token != null) "login" else "list"
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
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
