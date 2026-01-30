package ru.sicampus.bootcamp2026.ui.auth

data class RegisterUIState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,// для скрытия пароля, нажатие на глаз -> меняем на true
    val isConfirmPasswordVisible: Boolean = false,// тоже для скрытия
    val isLoading: Boolean = false,// флаг при отравке данных на бэк, блокируем повторные запросы и отрисовываем загрузку
    val error: String? = null// для вывода ошибки при попытке входа

)
