package ru.sicampus.bootcamp2026.ui.screen.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.InboxRepository
import ru.sicampus.bootcamp2026.data.source.InboxDataSource
import ru.sicampus.bootcamp2026.domain.inbox.GetActiveInvitationsListUseCase

class InboxViewModel() : ViewModel(){
    private val getActiveInvitationsListUseCase by lazy {
        GetActiveInvitationsListUseCase(
            repository = InboxRepository(
                inboxDataSource = InboxDataSource()
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






    fun onIntent(intent: InboxIntent) {
        when (intent) {
            is InboxIntent.AcceptInvitation -> TODO()
            is InboxIntent.DeclineInvitation -> TODO()
            is InboxIntent.LoadInvitations -> getData()
        }
    }
}
