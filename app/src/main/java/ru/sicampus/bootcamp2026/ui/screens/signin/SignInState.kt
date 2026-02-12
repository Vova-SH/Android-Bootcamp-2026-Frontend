package ru.sicampus.bootcamp2026.ui.screens.signin

sealed class SignInState {
    object Loading: SignInState()
    data class Data (
        val userLoggedIn: Boolean,
        val isEnabledSend: Boolean,
        val error: String?
    ): SignInState()
}