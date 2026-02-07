package ru.sicampus.bootcamp2026.ui.screen.list

import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity

sealed interface ListState {
    data class Error( val reason: String ): ListState
    data object Loading: ListState
    data class Content(
        val invitations: List<EventEntity>
    ): ListState
}