package com.example.navigation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authorization.presentation.AuthMainScreen
import com.example.authorization.presentation.AuthScreenViewModel
import com.example.registration.presentation.RegisterMainScreen
import com.example.registration.presentation.RegisterScreenViewModel
import com.example.token_storage.data.TokenRepositoryImpl
import com.example.token_storage.domain.SecureStorage

@Composable
fun AppNavigation(
    authViewModel: AuthScreenViewModel,
    registerScreenViewModel: RegisterScreenViewModel,
    navigationViewModel: NavigationViewModel
) {
    val navController = rememberNavController()
    val token by navigationViewModel.hasToken.collectAsState()


    NavHost(
        navController = navController,
        startDestination = if (!token) NavigationScreens.AUTHORIZATION.routeName else NavigationScreens.MAIN.routeName
    ) {
        composable(NavigationScreens.AUTHORIZATION.routeName) {
            AuthMainScreen(authViewModel){
                navController.navigate(NavigationScreens.REGISTER.routeName)
            }
        }

        composable(NavigationScreens.REGISTER.routeName) {
            RegisterMainScreen(registerScreenViewModel) {
                navController.navigate(NavigationScreens.MAIN.routeName)
            }
        }

        composable(NavigationScreens.MAIN.routeName) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "success"
                )
            }
        }

    }
}