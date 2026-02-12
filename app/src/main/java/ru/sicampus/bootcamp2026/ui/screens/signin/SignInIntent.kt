package ru.sicampus.bootcamp2026.ui.screens.signin

sealed interface SignInIntent {
    data class Send(val login: String, val password: String): SignInIntent
    data class TextInput(val login: String, val password: String): SignInIntent
    object Register: SignInIntent
}
