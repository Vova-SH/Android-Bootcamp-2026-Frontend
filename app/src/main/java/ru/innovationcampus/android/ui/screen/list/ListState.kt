package ru.innovationcampus.android.ui.screen.list

import kotlinx.collections.immutable.PersistentList
import ru.innovationcampus.android.domain.list.entities.UserEntity

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