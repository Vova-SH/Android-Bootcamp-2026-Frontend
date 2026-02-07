package ru.sicampus.bootcamp2026.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute(val route: String) {
    @Serializable
    object AuthRoute : AppRoute("auth")

    @Serializable
    object LoginRoute : AppRoute("login")

    @Serializable
    object RegisterRoute : AppRoute("register")

    @Serializable
    object ListRoute : AppRoute("list")
}