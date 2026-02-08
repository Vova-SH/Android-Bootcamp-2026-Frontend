package ru.sicampus.bootcamp2026.ui.screen.profile

import ru.sicampus.bootcamp2026.domain.entities.UserEntity

sealed interface ProfileState {
    data class Error(val reason: String, val buttonText: String = "Попробовать ещё раз", val onClickButton: () -> Unit): ProfileState
    data object Loading: ProfileState
    data class Content(
        val user: UserEntity,
        val isCurrentUser: Boolean = false
    ) : ProfileState
}