package ru.sicampus.bootcamp2026.ui.screens.schedule

import ru.sicampus.bootcamp2026.domain.entities.MeetingMini
import java.time.LocalDate

data class ScheduleUiState(
    val selectedPeriod: Period = Period.DAY,
    val selectedDate: LocalDate = LocalDate.now(),
    val dayMeetings: List<MeetingMini> = emptyList(),
    val weekMeetings: Map<LocalDate, List<MeetingMini>> = emptyMap(),
    val monthMeetings: Map<LocalDate, List<MeetingMini>> = emptyMap(),
    val errorMessage: String? = null
)