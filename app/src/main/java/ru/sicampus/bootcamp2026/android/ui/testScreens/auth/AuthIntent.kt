package ru.sicampus.bootcamp2026.android.ui.testScreens.auth

sealed interface AuthIntent {
    data class Send(val login: String, val password: String) : AuthIntent
    data class TextInput(val login: String, val password: String) : AuthIntent
    data object NavigateToSignUp : AuthIntent
}