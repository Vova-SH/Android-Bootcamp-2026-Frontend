package ru.sicampus.bootcamp2026.ui.trash

sealed class Route(val route: String) {
    object SingIn : Route("Вход")
    object SingUp : Route("Регистрация")

    object Home : Route("home")
    object Profile : Route("profile")
    object Settings : Route("settings")

    object Details : Route("details/{id}") {
        fun passId(id: Int) = "details/$id"
    }
}
