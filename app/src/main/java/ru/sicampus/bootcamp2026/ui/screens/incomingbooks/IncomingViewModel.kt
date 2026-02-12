package ru.sicampus.bootcamp2026.ui.screens.incomingbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.entities.InvitationStatus
import ru.sicampus.bootcamp2026.domain.usecase.invitation.GetInvitationsUseCase
import ru.sicampus.bootcamp2026.domain.usecase.invitation.RespondToInvitationUseCase

class IncomingViewModel(
    private val getInvitationsUseCase: GetInvitationsUseCase,
    private val respondToInvitationUseCase: RespondToInvitationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(IncomingUiState())
    val state: StateFlow<IncomingUiState> = _state.asStateFlow()

    init {
        loadInvitations()
    }

    fun loadInvitations() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getInvitationsUseCase()

            result.fold(
                onSuccess = { invitations ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            invitations = invitations
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Ошибка загрузки приглашений",
                            invitations = emptyList()
                        )
                    }
                }
            )
        }
    }

    fun respondToInvitation(invitationId: Long, accept: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val status = if (accept) InvitationStatus.ACCEPTED else InvitationStatus.DECLINED
            val result = respondToInvitationUseCase(invitationId, status)

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            invitations = _state.value.invitations.filter { it.id != invitationId }
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Ошибка отправки ответа"
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}