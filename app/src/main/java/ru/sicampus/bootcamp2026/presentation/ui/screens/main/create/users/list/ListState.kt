package ru.sicampus.bootcamp2026.presentation.ui.screens.main.create.users.list

sealed interface ListState {
    data object Loading: ListState
    data class Error(
        val reason: String
    ): ListState
    data class Content(
        val users: List<Any>
    ) : ListState

}