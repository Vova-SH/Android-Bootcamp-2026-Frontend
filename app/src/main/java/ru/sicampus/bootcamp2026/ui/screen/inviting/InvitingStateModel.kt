package ru.sicampus.bootcamp2026.ui.screen.inviting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.GetMyInvitationsUseCase
import ru.sicampus.bootcamp2026.domain.UpdateInvitationUseCase
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity
import ru.sicampus.bootcamp2026.util.ErrorUtils

class InvitingStateModel(
    private val getMyInvitationsUseCase: GetMyInvitationsUseCase = GetMyInvitationsUseCase(),
    private val updateInvitationUseCase: UpdateInvitationUseCase = UpdateInvitationUseCase()
) : ViewModel() {
    private val _uiState: MutableStateFlow<InvitingState> = MutableStateFlow(InvitingState.Content())
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.emit(InvitingState.Loading)
            getMyInvitationsUseCase().onSuccess { invitations ->
                _uiState.emit(InvitingState.Content(invitations = invitations))
            }.onFailure { e ->
                _uiState.emit(InvitingState.Error(ErrorUtils.translateError(e, "Неизвестная ошибка")))
            }
        }
    }

    fun respondToInvitation(invitation: InvitationEntity, status: Int) {
        viewModelScope.launch {
            val updatedInvitation = invitation.copy(status = status)
            updateInvitationUseCase(updatedInvitation).onSuccess {
                getData() 
            }.onFailure { e ->
                _uiState.emit(InvitingState.Error(ErrorUtils.translateError(e, "Не удалось обновить приглашение")))
            }
        }
    }
}
