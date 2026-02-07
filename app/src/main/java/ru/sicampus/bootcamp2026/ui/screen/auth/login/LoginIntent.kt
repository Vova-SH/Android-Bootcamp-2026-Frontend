package ru.sicampus.bootcamp2026.ui.screen.auth.login

sealed interface LoginIntent {
    data class Send(val login: String, val password: String): LoginIntent
    data class TextInput(val login: String, val password: String): LoginIntent
}