package ru.sicampus.bootcamp2026.android.ui.testScreens.auth

sealed interface AuthState {
    object Loading: AuthState
    data class Data(
        val isEnabledSend: Boolean,
        val error: String?
    ): AuthState
}