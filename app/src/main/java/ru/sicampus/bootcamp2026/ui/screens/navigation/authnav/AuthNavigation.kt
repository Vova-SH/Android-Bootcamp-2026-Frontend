package ru.sicampus.bootcamp2026.ui.screens.navigation.authnav

sealed class AuthNavigation(
    val route: String
) {
    object Auth: AuthNavigation(
        route = "AuthorizationScreen"
    )

    object Profile: AuthNavigation(
        route = "ProfileScreen"
    )
}