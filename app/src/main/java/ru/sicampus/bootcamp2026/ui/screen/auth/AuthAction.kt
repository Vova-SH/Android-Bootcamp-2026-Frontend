package ru.sicampus.bootcamp2026.ui.screen.auth

sealed interface AuthAction {
    data class OpenScreen(val route: String): AuthAction // ← принимаем строку
}