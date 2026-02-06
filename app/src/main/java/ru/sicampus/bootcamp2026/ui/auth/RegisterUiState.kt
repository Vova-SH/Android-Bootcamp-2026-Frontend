package ru.sicampus.bootcamp2026.ui.auth

/**
 * UI состояние для экрана регистрации
 */
data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    // Ошибки валидации
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    // Состояния
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

/**
 * События UI для экрана регистрации
 */
sealed interface RegisterUiEvent {
    data class UsernameChanged(val username: String) : RegisterUiEvent
    data class EmailChanged(val email: String) : RegisterUiEvent
    data class PasswordChanged(val password: String) : RegisterUiEvent
    data class ConfirmPasswordChanged(val password: String) : RegisterUiEvent
    data object Register : RegisterUiEvent
    data object DismissError : RegisterUiEvent
}

