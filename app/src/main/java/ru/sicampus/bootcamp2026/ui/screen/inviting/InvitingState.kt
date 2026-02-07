package ru.sicampus.bootcamp2026.ui.screen.inviting

import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

sealed interface InvitingState {
    data class Error(val reason: String) : InvitingState
    data object Loading : InvitingState
    data class Content(
        val invitations: List<InvitationEntity> = emptyList()
    ) : InvitingState
}
