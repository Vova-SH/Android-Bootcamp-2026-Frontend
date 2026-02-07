package ru.sicampus.bootcamp2026.ui.login

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.login.nav.LoginScreenDestination
import ru.sicampus.bootcamp2026.ui.login.nav.RegisterScreenDestination
import ru.sicampus.bootcamp2026.ui.screens.auth.login.LoginScreen
import ru.sicampus.bootcamp2026.ui.screens.auth.signup.SignUpScreen

@Composable
fun LoginNavHost(
    navController: NavHostController = rememberNavController(),
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = LoginScreenDestination
    ) {
        composable<LoginScreenDestination> {
            LoginScreen(context = context, navController = navController)
        }

        composable<RegisterScreenDestination> {
            SignUpScreen(context = context, navController =  navController)
        }
    }
}