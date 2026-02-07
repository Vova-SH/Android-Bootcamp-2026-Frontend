package ru.sicampus.bootcamp2026.ui.screen.main

import ru.sicampus.bootcamp2026.domain.entities.UserEntity

data class MainState(
    val meetingName: String = "",
    val meetingDate: String = "",
    val meetingTime: String = "",
    val searchQuery: String = "",
    val foundUsers: List<UserEntity> = emptyList(),
    val selectedUsers: List<UserEntity> = emptyList(),
    val currentUser: UserEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
