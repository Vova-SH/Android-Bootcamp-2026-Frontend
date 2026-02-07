package ru.sicampus.bootcamp2026.ui.theme.screens.Login

sealed interface LoginState {
    data class Error(val reason: String): LoginState
    data object Loading: LoginState
    data object Content:LoginState
    data object Reg:LoginState
}