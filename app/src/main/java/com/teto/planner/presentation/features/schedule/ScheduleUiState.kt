package com.teto.planner.presentation.features.schedule

import com.teto.planner.domain.model.meeting.Meeting
import java.time.LocalDate

sealed interface ScheduleUiState {
    data object Loading : ScheduleUiState

    data class Success(
        val meetings: List<Meeting>,
        val meetingsForSelectedDate: List<Meeting>,
        val currentUserId: String,
        val selectedDate: LocalDate = LocalDate.now(),
        val selectedMeeting: Meeting? = null,
        val meetingToEdit: Meeting? = null,
        val isMeetingDetailsLoading: Boolean = false,
        val meetingDetailsError: String? = null,
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false
    ) : ScheduleUiState {
        val meetingsByDate: Map<LocalDate, List<Meeting>>
            get() = meetings.groupBy { it.date }
    }

    data class Error(val message: String) : ScheduleUiState
}