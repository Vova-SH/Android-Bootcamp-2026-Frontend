package ru.sicampus.bootcamp2026.ui.meeting

import ru.sicampus.bootcamp2026.domain.model.Meeting
import java.util.UUID

/**
 * UI состояние для экрана деталей встречи
 */
data class MeetingDetailUiState(
    val meeting: Meeting? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val shouldNavigateBack: Boolean = false
)

/**
 * События UI для экрана деталей встречи
 */
sealed interface MeetingDetailUiEvent {
    data class LoadMeeting(val meetingId: UUID) : MeetingDetailUiEvent
    data object CancelMeeting : MeetingDetailUiEvent
    data object DeleteMeeting : MeetingDetailUiEvent
    data object DismissError : MeetingDetailUiEvent
    data object DismissSuccess : MeetingDetailUiEvent
}
