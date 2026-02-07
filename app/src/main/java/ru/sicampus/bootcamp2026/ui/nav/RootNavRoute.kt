package ru.sicampus.bootcamp2026.ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.domain.auth.AuthState
import ru.sicampus.bootcamp2026.ui.MainScreen
import ru.sicampus.bootcamp2026.ui.screen.auth.login.LoginScreen
import ru.sicampus.bootcamp2026.ui.screen.auth.register.RegisterScreen

@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box {
            when (authState) {
                is AuthState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthState.Unauthorized -> {
                    val loginNavController = rememberNavController()
                    NavHost(
                        navController = loginNavController,
                        startDestination = AppRoute.LoginRoute,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable<AppRoute.LoginRoute> {
                            LoginScreen(
                                onNavigateToRegister = { loginNavController.navigate(AppRoute.RegisterRoute) },
                                navController = loginNavController
                            )
                        }
                        composable<AppRoute.RegisterRoute> {
                            RegisterScreen(navController = loginNavController)
                        }
                    }
                }

                is AuthState.Authorized -> {
                    MainScreen()
                }
            }
        }
    }
}