package ru.innovationcampus.android.ui.screen.auth

sealed interface AuthState {
    object Loading: AuthState
    data class Data(
        val isEnabledSend: Boolean,
        val error: String?
    ): AuthState
}