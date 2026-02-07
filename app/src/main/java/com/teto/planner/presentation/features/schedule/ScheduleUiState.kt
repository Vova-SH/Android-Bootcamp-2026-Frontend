package com.teto.planner.presentation.features.schedule

import com.teto.planner.domain.model.meeting.Meeting
import java.time.LocalDate

sealed interface ScheduleUiState {
    data object Loading : ScheduleUiState

    data class Success(
        val meetings: List<Meeting>,
        val selectedDate: LocalDate = LocalDate.now(),
        val selectedMeeting: Meeting? = null,
        val isMeetingDetailsLoading: Boolean = false,
        val meetingDetailsError: String? = null
    ) : ScheduleUiState {
        val meetingsForSelectedDate: List<Meeting>
            get() = meetings.filter { it.date == selectedDate }
        val meetingsByDate: Map<LocalDate, List<Meeting>>
            get() = meetings.groupBy { it.date }
    }
    data class Error(val message: String) : ScheduleUiState
}
