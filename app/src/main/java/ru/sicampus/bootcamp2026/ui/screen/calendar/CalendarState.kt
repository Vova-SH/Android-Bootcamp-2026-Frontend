package ru.sicampus.bootcamp2026.ui.screen.calendar

import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity

sealed interface CalendarState {
    data class Error( val reason: String ): CalendarState
    data object Loading: CalendarState
    data class Content(
        val events: List<EventEntity>
    ): CalendarState
}