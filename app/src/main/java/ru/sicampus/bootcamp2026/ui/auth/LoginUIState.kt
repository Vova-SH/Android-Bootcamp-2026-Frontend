package ru.sicampus.bootcamp2026.ui.auth

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,// для скрытия пароля, нажатие на глаз -> меняем на true
    val isLoading: Boolean = false,// флаг при отравке данных на бэк, блокируем повторные запросы и отрисовываем загрузку
    val error: String? = null// для вывода ошибки при попытке входа
)
