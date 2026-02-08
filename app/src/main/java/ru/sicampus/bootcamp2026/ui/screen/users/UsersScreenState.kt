package ru.sicampus.bootcamp2026.ui.screen.users

import ru.sicampus.bootcamp2026.domain.entities.UserEntity

sealed interface UsersScreenState {
    data class Error(val reason: String, val buttonText: String = "Попробовать ещё раз", val onClickButton: () -> Unit): UsersScreenState
    data object Loading: UsersScreenState
    data class Content(
        val users: List<UserEntity>
    ) : UsersScreenState
}