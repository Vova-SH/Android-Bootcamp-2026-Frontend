package com.example.create_meet.presentation

import androidx.compose.runtime.Immutable
import com.example.create_meet.data.dto.MeetingResponse


@Immutable
sealed class EventsUiState {
    object Loading : EventsUiState()
    data class Success(val meetings: List<MeetingResponse>) : EventsUiState()
    data class Error(val message: String) : EventsUiState()

    object NotLoaded: EventsUiState()
}