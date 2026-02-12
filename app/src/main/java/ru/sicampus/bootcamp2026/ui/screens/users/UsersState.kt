package ru.sicampus.bootcamp2026.ui.screens.users

import kotlinx.collections.immutable.PersistentList
import ru.sicampus.bootcamp2026.domain.users.entities.UserEntity

sealed interface UsersState {
    data class Error(val reason: String): UsersState
    data object Loading: UsersState
    data class Content(
        val isLastPage: Boolean,
        val users: PersistentList<Item>
    ): UsersState

    sealed interface Item {
        data object Loading: Item
        data object Error: Item
        data class User(val userEntity: UserEntity): Item
    }
}
