package ru.sicampus.bootcamp2026.ui.screen.schedule

import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import java.time.LocalDate

sealed interface ScheduleState {
    data class Error(val reason: String) : ScheduleState
    data object Loading : ScheduleState
    data class Content(
        val meetings: List<MeetingEntity> = emptyList(),
        val selectedDate: LocalDate = LocalDate.now()
    ) : ScheduleState
}
