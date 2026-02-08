package ru.sicampus.bootcamp2026.ui.screen.users

import ru.sicampus.bootcamp2026.data.dto.UserDto

sealed interface UserDetailsUiState {
    data object Loading : UserDetailsUiState
    data class Error(val message: String) : UserDetailsUiState
    data class Content(val user: UserDto) : UserDetailsUiState
}