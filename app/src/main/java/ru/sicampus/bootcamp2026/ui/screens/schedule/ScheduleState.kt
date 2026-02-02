package ru.sicampus.bootcamp2026.ui.screens.schedule

import ru.sicampus.bootcamp2026.domain.entities.CreateMeetingEntity
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import ru.sicampus.bootcamp2026.ui.screens.createinvite.CreateInviteState
import java.time.LocalDate

sealed interface ScheduleState {
    data class Error(val reason: String) : ScheduleState
    data object Loading : ScheduleState
    data class Content(
        val today: LocalDate,
        val days: List<LocalDate>,
        val meetings: List<MeetingEntity>
    ) : ScheduleState
}