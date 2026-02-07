package ru.sicampus.bootcamp2026.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel для экрана уведомлений/приглашений
 */
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val invitationRepository: InvitationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInvitations()
    }

    fun onEvent(event: NotificationUiEvent) {
        when (event) {
            is NotificationUiEvent.LoadInvitations -> loadInvitations()
            is NotificationUiEvent.RefreshInvitations -> refreshInvitations()
            is NotificationUiEvent.AcceptInvitation -> acceptInvitation(event.invitationId)
            is NotificationUiEvent.DeclineInvitation -> declineInvitation(event.invitationId)
            is NotificationUiEvent.DismissError -> {
                _uiState.update { it.copy(error = null) }
            }
            is NotificationUiEvent.DismissSuccess -> {
                _uiState.update { it.copy(successMessage = null) }
            }
        }
    }

    private fun loadInvitations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = invitationRepository.getInvitations(0, 50)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            invitations = result.data.content,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to load invitations: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun refreshInvitations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            val result = invitationRepository.getInvitations(0, 50)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            invitations = result.data.content,
                            isRefreshing = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to refresh: ${result.exception.message}",
                            isRefreshing = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun acceptInvitation(invitationId: UUID) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = invitationRepository.respondToInvitation(invitationId, true)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            successMessage = "Invitation accepted",
                            isLoading = false
                        )
                    }
                    // Перезагружаем список после принятия
                    loadInvitations()
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to accept invitation: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun declineInvitation(invitationId: UUID) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = invitationRepository.respondToInvitation(invitationId, false)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            successMessage = "Invitation declined",
                            isLoading = false
                        )
                    }
                    // Перезагружаем список после отклонения
                    loadInvitations()
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            error = "Failed to decline invitation: ${result.exception.message}",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }
}

