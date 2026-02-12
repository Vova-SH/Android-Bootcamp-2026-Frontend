package ru.sicampus.bootcamp2026.ui.screen.inbox

sealed interface InboxIntent {
    data object LoadInvitations : InboxIntent
    data class AcceptInvitation(val invitationId: Int) : InboxIntent
    data class DeclineInvitation(val invitationId: Int) : InboxIntent
}