package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.LoginScreen
import ru.sicampus.bootcamp2026.ui.theme.MainHomeScreen
import ru.sicampus.bootcamp2026.ui.theme.InviteScreen
import ru.sicampus.bootcamp2026.ui.theme.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("registration") {
                        RegistrationScreen(navController = navController)
                    }
                    composable("main") {
                        MainHomeScreen(navController = navController)
                    }
                    composable("meetings") {
                        InviteScreen(navController = navController)
                    }
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}
