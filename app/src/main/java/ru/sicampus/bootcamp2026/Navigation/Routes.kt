package ru.sicampus.bootcamp2026.navigation

sealed class Routes(val route: String) {
    data object Auth : Routes("auth")
    data object Registration : Routes("registration")
    data object Profile : Routes("profile")
    data object Invitations : Routes("invitations")
    data object Home : Routes("home")
    data object Schedule : Routes("schedule")
}
