package com.example.registration.presentation

import com.example.comon.LoginResponseDto

sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    object Success: RegisterUiState
    data class Error(val message: String) : RegisterUiState
}