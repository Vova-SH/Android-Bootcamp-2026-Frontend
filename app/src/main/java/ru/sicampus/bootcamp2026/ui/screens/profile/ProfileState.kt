package ru.sicampus.bootcamp2026.ui.screens.profile

import ru.sicampus.bootcamp2026.domain.entities.UserEntity

sealed interface ProfileState {
    data class Error(val reason: String): ProfileState
    data object Loading: ProfileState
    data class Content(
        val currentUser: UserEntity
    ): ProfileState
}