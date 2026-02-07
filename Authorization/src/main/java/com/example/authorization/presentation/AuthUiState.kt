package com.example.authorization.presentation

sealed class AuthUiState {
    object Idle: AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}