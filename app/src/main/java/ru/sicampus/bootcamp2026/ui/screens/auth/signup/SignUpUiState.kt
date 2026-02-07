package ru.sicampus.bootcamp2026.ui.screens.auth.signup

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val password2: String = "",
    val name: String = "",
    val surname: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccess: Boolean = false
)
