package ru.sicampus.bootcamp2026.ui.theme.state

sealed interface AuthState{

    object Loading: AuthState
        data class Data(
            val isEnabledSend: Boolean,
            val error: String?
        ): AuthState


}