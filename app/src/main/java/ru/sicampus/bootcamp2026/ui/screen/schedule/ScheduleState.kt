package ru.sicampus.bootcamp2026.ui.screen.schedule

import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

sealed interface ScheduleState {
    data class Error(val reason: String) : ScheduleState
    data object Loading : ScheduleState
    data class Content(val meetings: List<MeetingEntity> = emptyList()) : ScheduleState
}
