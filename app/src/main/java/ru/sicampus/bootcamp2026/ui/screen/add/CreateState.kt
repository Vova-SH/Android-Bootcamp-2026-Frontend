package ru.sicampus.bootcamp2026.ui.screen.add

sealed interface CreateState {
    data class Error( val reason: String ): CreateState
    data object Loading: CreateState
    data class Content(
        val users: Unit
    ): CreateState
}