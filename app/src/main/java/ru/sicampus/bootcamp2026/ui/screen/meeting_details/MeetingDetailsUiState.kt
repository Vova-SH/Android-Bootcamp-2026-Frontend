package ru.sicampus.bootcamp2026.ui.screen.meeting_details

import ru.sicampus.bootcamp2026.data.dto.MemberDto
import ru.sicampus.bootcamp2026.domain.model.Meeting

sealed interface MeetingDetailsUiState {
    data object Loading : MeetingDetailsUiState
    data class Error(val message: String) : MeetingDetailsUiState
    data class Content(val meeting: Meeting, val members: List<MemberDto>) : MeetingDetailsUiState
}