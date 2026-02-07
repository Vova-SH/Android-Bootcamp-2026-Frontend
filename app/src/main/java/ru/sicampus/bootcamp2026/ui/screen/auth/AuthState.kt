package ru.sicampus.bootcamp2026.ui.screen.auth

data class AuthState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthorized: Boolean = false,
    val isEnabledSend: Boolean = false
)