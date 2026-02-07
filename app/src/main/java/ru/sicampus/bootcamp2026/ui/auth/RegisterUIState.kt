package ru.sicampus.bootcamp2026.ui.auth

data class RegisterUIState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,// для скрытия пароля, нажатие на глаз -> меняем на true
    val isConfirmPasswordVisible: Boolean = false,// тоже для скрытия
    val isLoading: Boolean = false,// флаг при отравке данных на бэк, блокируем повторные запросы и отрисовываем загрузку
    val error: String? = null,// для вывода ошибки при попытке входа
    val isSuccess: Boolean = false// флаг успешной регистрации для навигации
)

/**
 * События UI для экрана регистрации
 */
sealed interface RegisterUiEvent {
    data class UsernameChanged(val username: String) : RegisterUiEvent
    data class EmailChanged(val email: String) : RegisterUiEvent
    data class PasswordChanged(val password: String) : RegisterUiEvent
    data class ConfirmPasswordChanged(val password: String) : RegisterUiEvent
    data object TogglePasswordVisibility : RegisterUiEvent
    data object ToggleConfirmPasswordVisibility : RegisterUiEvent
    data object Register : RegisterUiEvent
    data object DismissError : RegisterUiEvent
}

