package com.teto.planner.presentation.features.invitations

import com.teto.planner.domain.model.meeting.Invitation

sealed interface InvitationUiState {
    object Loading : InvitationUiState

    data class Success(
        val invitations: List<Invitation> = emptyList()
    ) : InvitationUiState

    data class Error(val message: String) : InvitationUiState

    object Empty : InvitationUiState
}