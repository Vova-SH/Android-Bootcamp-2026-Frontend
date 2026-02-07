package com.teto.planner.presentation.features.auth.login

sealed interface LoginUiState {
    data object Loading : LoginUiState

    data class Content(
        val login: String = "",
        val password: String = "",
        val isSubmitting: Boolean = false,
        val errorMessage: String? = null,
        val isLoginSuccessful: Boolean = false
    ) : LoginUiState {
        val canSubmit: Boolean
            get() = login.isNotBlank() && password.isNotBlank() && !isSubmitting
    }
}