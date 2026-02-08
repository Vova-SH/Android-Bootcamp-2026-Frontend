package ru.innovationcampus.android.ui.screen.auth

sealed interface AuthIntent {
    data class Send(val login: String, val password: String): AuthIntent
    data class TextInput(val login: String, val password: String): AuthIntent
}