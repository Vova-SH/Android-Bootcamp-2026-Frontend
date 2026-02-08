package ru.sicampus.bootcamp2026.ui.screen.auth


sealed interface AuthIntent {
    data class Send(val email: String, val password: String): AuthIntent
    data class TextInput(val email: String, val password: String): AuthIntent
}