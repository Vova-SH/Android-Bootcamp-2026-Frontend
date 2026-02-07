package com.teto.planner.presentation.features.auth.register

sealed interface RegisterUiState {
    data object Loading : RegisterUiState

    data class Content(
        val name: String = "",
        val login: String = "",
        val password: String = "",
        val telegramNick: String = "",
        val isSubmitting: Boolean = false,
        val errorMessage: String? = null,
        val isRegisterSuccessful: Boolean = false
    ) : RegisterUiState {
        val canSubmit: Boolean
            get() = name.isNotBlank() && login.isNotBlank() && password.isNotBlank() && !isSubmitting
    }
}