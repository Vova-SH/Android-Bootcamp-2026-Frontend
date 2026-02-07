package ru.sicampus.bootcamp2026.ui.screen.inbox

sealed interface InboxIntent {
    data object LoadInvitations : InboxIntent
    data class AcceptInvitation(val invitationId: String) : InboxIntent
    data class DeclineInvitation(val invitationId: String) : InboxIntent
}