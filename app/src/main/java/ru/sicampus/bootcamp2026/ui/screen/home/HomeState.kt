package ru.sicampus.bootcamp2026.ui.screen.home

import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity

sealed interface HomeState {
    data class Error( val reason: String ): HomeState
    data object Loading: HomeState
    data class Content(
        val events: List<EventEntity>
    ): HomeState
}