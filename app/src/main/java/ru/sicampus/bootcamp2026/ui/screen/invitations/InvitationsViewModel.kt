package ru.sicampus.bootcamp2026.ui.screen.invitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.usecase.AcceptInvitationUseCase
import ru.sicampus.bootcamp2026.domain.usecase.GetInvitationsUseCase
import ru.sicampus.bootcamp2026.domain.usecase.RejectInvitationUseCase

class InvitationsViewModel(
    private val getInvitationsUseCase: GetInvitationsUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val rejectInvitationUseCase: RejectInvitationUseCase
) : ViewModel() {

    private val _invitations = MutableStateFlow<List<Invitation>>(emptyList())
    val invitations = _invitations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadInvitations()
    }

    fun loadInvitations() {
        viewModelScope.launch {
            _isLoading.value = true
            getInvitationsUseCase()
                .onSuccess { _invitations.value = it }
                .onFailure { _error.value = "Failed to load: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun respondToInvitation(id: Long, accept: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = if (accept) acceptInvitationUseCase(id) else rejectInvitationUseCase(id)

            result.onSuccess {
                loadInvitations()
            }.onFailure {
                _error.value = "Action failed: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun clearError() { _error.value = null }
}