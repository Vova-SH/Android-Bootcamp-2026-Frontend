package ru.sicampus.bootcamp2026.ui.screen.inbox

import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

sealed interface InboxState {
    data object Loading: InboxState
    data class Error(
        val reason: String
    ) : InboxState
    data class Content(
        val invitations: List<InvitationEntity>
    ) : InboxState
}