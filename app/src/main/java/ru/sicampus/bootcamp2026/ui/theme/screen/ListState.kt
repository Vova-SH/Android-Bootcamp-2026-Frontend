package ru.sicampus.bootcamp2026.ui.theme.screen

import ru.sicampus.bootcamp2026.domain.entities.UserEntity

import kotlinx.collections.immutable.PersistentList
sealed interface ListState {
    data class Error(val reason: String): ListState
    data object Loading: ListState
    data class Content(
        val isLastPage: Boolean,
        val users: PersistentList<Item>
    ): ListState

    sealed interface Item {
        data object Loading: Item
        data object Error: Item
        data class User(val entity: UserEntity): Item
    }
}