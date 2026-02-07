package ru.sicampus.bootcamp2026.ui.screen.add


import kotlinx.collections.immutable.PersistentList
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity


sealed interface AddState {
    data class Error( val reason: String ): AddState
    data object Loading: AddState
    data class Content(
        val isLastPage: Boolean,
        val users: PersistentList<Item>
    ): AddState

    sealed interface Item{
        data object Loading: Item
        data object Error: Item
        data class User(val entity: UserEntity) : Item
    }
}