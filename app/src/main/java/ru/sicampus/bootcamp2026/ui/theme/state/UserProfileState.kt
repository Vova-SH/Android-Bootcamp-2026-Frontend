package ru.sicampus.bootcamp2026.ui.theme.state

import ru.sicampus.bootcamp2026.data.dto.UserDto



sealed interface UserProfileState {
    object Loading : UserProfileState
    data class Success(val user: UserDto) : UserProfileState
    data class Error(val message: String) : UserProfileState
}
