package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.screens.LoginScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.RegistrationScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.MainHomeScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.InviteScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                val userInfoDataSource = UserInfoDataSource()
                val userRepository = UserRepository(userInfoDataSource)
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            userRepository = userRepository
                        )
                    }
                    composable("registration") {
                        RegistrationScreen(
                            navController = navController,
                            userRepository = userRepository
                        )
                    }
                    composable("main") {
                        MainHomeScreen(
                            navController = navController,
                            userRepository = userRepository
                        )
                    }
                    composable("meetings") {
                        InviteScreen(
                            navController = navController,
                            userRepository = userRepository
                        )
                    }
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}
