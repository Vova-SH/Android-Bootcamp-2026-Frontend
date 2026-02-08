package ru.sicampus.bootcamp2026.ui.screen.meeting_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.dto.MemberDto
import ru.sicampus.bootcamp2026.data.source.TokenStorage
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.usecase.*

class MeetingDetailsViewModel(
    private val getMeetingByIdUseCase: GetMeetingByIdUseCase,
    private val getMeetingMembersUseCase: GetMeetingMembersUseCase,
    private val updateMeetingUseCase: UpdateMeetingUseCase,
    private val sendInvitationUseCase: SendInvitationUseCase,
    private val deleteMeetingUseCase: DeleteMeetingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MeetingDetailsUiState>(MeetingDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage = _actionMessage.asStateFlow()

    private val _navigationEvent = MutableStateFlow<Boolean>(false)
    val navigationEvent = _navigationEvent.asStateFlow()

    fun loadData(meetingId: Long) {
        viewModelScope.launch {
            _uiState.value = MeetingDetailsUiState.Loading

            val meetingResult = getMeetingByIdUseCase(meetingId)

            if (meetingResult.isSuccess) {
                val meeting = meetingResult.getOrThrow()
                val membersResult = getMeetingMembersUseCase(meetingId)
                val members = membersResult.getOrThrow()

                _uiState.value = MeetingDetailsUiState.Content(meeting, members)
            } else {
                _uiState.value = MeetingDetailsUiState.Error(meetingResult.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun sendInvite(meetingId: Long, email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            sendInvitationUseCase(email, meetingId)
                .onSuccess { _actionMessage.value = "Приглашение отправлено: $email" }
                .onFailure { _actionMessage.value = "Ошибка отправки: ${it.message}" }
        }
    }

    fun updateMeeting(id: Long, title: String, desc: String, place: String, start: String, duration: Int) {
        viewModelScope.launch {
            updateMeetingUseCase(id, title, desc, place, start, duration)
                .onSuccess {
                    _actionMessage.value = "Встреча обновлена"
                    loadData(id)
                }
                .onFailure { _actionMessage.value = "Ошибка обновления: ${it.message}" }
        }
    }

    fun deleteMeeting(id: Long) {
        viewModelScope.launch {
            deleteMeetingUseCase(id)
                .onSuccess { _navigationEvent.value = true }
                .onFailure { _actionMessage.value = "Не удалось удалить: ${it.message}" }
        }
    }

    fun clearMessage() { _actionMessage.value = null }
}