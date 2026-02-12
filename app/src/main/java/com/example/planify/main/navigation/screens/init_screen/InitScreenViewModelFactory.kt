package com.example.planify.main.navigation.screens.init_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.planify.main.features.auth.domain.services.AuthService

class InitScreenViewModelFactory(
    val authService: AuthService,
    val navHostController: NavHostController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InitScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InitScreenViewModel(
                authService = authService,
                navHostController = navHostController
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}