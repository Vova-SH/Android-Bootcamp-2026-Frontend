package ru.sicampus.bootcamp2026.ui.screens.incomingbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto


data class IncomingState(
    val invitations: List<MeetingMiniDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class IncomingViewModel(
    //private val getInvitationsUseCase: GetInvitationsUseCase,
    //private val respondToInvitationUseCase: RespondToInvitationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(IncomingState())
    val state: StateFlow<IncomingState> = _state.asStateFlow()

    init {
        loadInvitations()
    }

    fun loadInvitations() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

//            val result = getInvitationsUseCase("pending")
//
//            result.fold(
//                onSuccess = { invitations ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        invitations = invitations
//                    )
//                },
//                onFailure = { error ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = error.message ?: "Ошибка загрузки приглашений",
//                        invitations = emptyList()
//                    )
//                }
//            )
        }
    }

    fun respondToInvitation(meetingId: Long, accept: Boolean) {
        viewModelScope.launch {
            //_state.value = _state.value.copy(ival result = respondToInvitationUseCase(meetingId, accept)
//
//            result.fold(
//                onSuccess = {
//                    // Удалить приглашение из списка после ответа
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        invitations = _state.value.invitations.filter { it.id != meetingId }
//                    )
//                },
//                onFailure = { error ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = error.message ?: "Ошибка отправки ответа"
//                    )
//                }
//            )sLoading = true, error = null)

//
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}