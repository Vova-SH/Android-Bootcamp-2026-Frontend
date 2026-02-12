package com.example.planify.main.navigation.screens.init_screen.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.navigation.AppRoute
import com.example.planify.main.navigation.screens.init_screen.InitScreenViewModel
import com.example.planify.main.navigation.screens.init_screen.UIState
import com.example.planify.main.navigation.screens.init_screen.components.LoadingView

@Composable
private fun InitScreen(
    viewModel: InitScreenViewModel,
    navHostController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { route ->
            navHostController.navigate(route.route) {
                popUpTo(AppRoute.Auth.route) { inclusive = true }
            }
        }
    }

    when (state) {
        UIState.LOADING -> LoadingView()
        UIState.SERVER_NOT_AVAILABLE -> Text("Error 500")
    }
}

@Composable
fun InitScreen(
    navHostController: NavHostController
) {
    InitScreen(viewModel = hiltViewModel(), navHostController = navHostController)
}
