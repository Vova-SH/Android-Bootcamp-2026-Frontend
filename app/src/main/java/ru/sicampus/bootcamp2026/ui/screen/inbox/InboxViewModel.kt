package ru.sicampus.bootcamp2026.ui.screen.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.repository.InvitationRepository
import ru.sicampus.bootcamp2026.data.source.InvitationDataSource
import ru.sicampus.bootcamp2026.domain.inbox.GetActiveInvitationsListUseCase

class InboxViewModel() : ViewModel(){
    private val getActiveInvitationsListUseCase by lazy {
        GetActiveInvitationsListUseCase(
            repository = InvitationRepository(
                invitationDataSource = InvitationDataSource()
            )
        )
    }
    private val answerInvitationUseCase by lazy {
        ru.sicampus.bootcamp2026.domain.inbox.AnswerInvitationUseCase(
            repo = InvitationRepository(
                invitationDataSource = InvitationDataSource()
            )
        )
    }

    private val _uiState : MutableStateFlow<InboxState> =
        MutableStateFlow(InboxState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }
    private fun getData(){
        viewModelScope.launch {
            _uiState.value = InboxState.Loading
            getActiveInvitationsListUseCase().fold(
                onSuccess = { invitations ->
                    _uiState.value = InboxState.Content(invitations)
                },
                onFailure = { e ->
                    _uiState.value = InboxState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }
    private fun answerInvitation(id: Int, isAccepted: Boolean) {
        viewModelScope.launch {
            answerInvitationUseCase(id, isAccepted).fold(
                onSuccess = {
                    getData()
                },
                onFailure = { e ->
                    _uiState.value = InboxState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }

    fun onIntent(intent: InboxIntent) {
        when (intent) {
            is InboxIntent.AcceptInvitation -> answerInvitation(intent.invitationId, true)
            is InboxIntent.DeclineInvitation -> answerInvitation(intent.invitationId, false)
            is InboxIntent.LoadInvitations -> getData()
        }
    }
}
