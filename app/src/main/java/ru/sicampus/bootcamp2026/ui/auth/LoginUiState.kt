package ru.sicampus.bootcamp2026.ui.auth

/**
 * UI состояние для экрана входа
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",

    // Ошибки валидации
    val emailError: String? = null,
    val passwordError: String? = null,

    // Состояния
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

/**
 * События UI для экрана входа
 */
sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object Login : LoginUiEvent
    data object DismissError : LoginUiEvent
}

