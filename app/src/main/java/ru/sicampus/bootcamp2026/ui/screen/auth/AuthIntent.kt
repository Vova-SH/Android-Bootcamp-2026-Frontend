package ru.sicampus.bootcamp2026.ui.screen.auth

sealed class AuthIntent {
    data class Send(val login: String, val password: String) : AuthIntent()
    data class TextInput(val login: String, val password: String) : AuthIntent()
}
