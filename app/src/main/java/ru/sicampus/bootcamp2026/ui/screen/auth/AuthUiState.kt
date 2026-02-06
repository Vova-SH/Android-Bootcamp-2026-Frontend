package ru.sicampus.bootcamp2026.ui.screen.auth

import ru.sicampus.bootcamp2026.data.dto.UserDto

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: UserDto) : AuthUiState
    data class Error(val message: String) : AuthUiState
}