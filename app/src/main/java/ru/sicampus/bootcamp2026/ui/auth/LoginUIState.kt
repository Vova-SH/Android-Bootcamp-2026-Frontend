package ru.sicampus.bootcamp2026.ui.auth

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,// для скрытия пароля, нажатие на глаз -> меняем на true
    val isLoading: Boolean = false,// флаг при отравке данных на бэк, блокируем повторные запросы и отрисовываем загрузку
    val error: String? = null,// для вывода ошибки при попытке входа
    val isSuccess: Boolean = false// флаг успешного входа для навигации
)

/**
 * События UI для экрана входа
 */
sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object TogglePasswordVisibility : LoginUiEvent
    data object Login : LoginUiEvent
    data object DismissError : LoginUiEvent
}

