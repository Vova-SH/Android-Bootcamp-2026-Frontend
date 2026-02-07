package ru.sicampus.bootcamp2026.ui.calendar

import ru.sicampus.bootcamp2026.domain.model.Meeting
import java.time.LocalDate

/**
 * UI состояние для экрана календаря
 */
data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val meetings: List<Meeting> = emptyList(),
    val meetingsForSelectedDate: List<Meeting> = emptyList(),
    val currentMonth: LocalDate = LocalDate.now(),

    // Состояния
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

/**
 * События UI для экрана календаря
 */
sealed interface CalendarUiEvent {
    data object LoadMeetings : CalendarUiEvent
    data object RefreshMeetings : CalendarUiEvent
    data class SelectDate(val date: LocalDate) : CalendarUiEvent
    data class ChangeMonth(val month: LocalDate) : CalendarUiEvent
    data object DismissError : CalendarUiEvent
}

