package com.example.user_main.presentation

import com.example.comon.User

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: String) : UserUiState()

    object NotLoaded : UserUiState()
}