package ru.innovationcampus.android.ui.screen.list

sealed interface ListState {
    data class Error(val reason: String): ListState
    data object Loading: ListState
    data class Content(
        val users: List<ru.sicampus.bootcamp2026.domain.entities.UserEntity>
    ): ListState
}