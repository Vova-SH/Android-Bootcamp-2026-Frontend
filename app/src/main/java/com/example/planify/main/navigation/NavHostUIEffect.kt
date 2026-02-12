package com.example.planify.main.navigation

sealed interface NavHostUIEffect {
    data class Navigate(val route: AppRoute) : NavHostUIEffect
    data class ShowDialog(val dialog: DialogType) : NavHostUIEffect
}