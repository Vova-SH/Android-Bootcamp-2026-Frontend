package ru.sicampus.bootcamp2026.ui.theme.screens.Invitation

sealed interface InvitationState{
    data class Error(val reason: String):InvitationState
    data object Loading: InvitationState
    data object Meetings: InvitationState

}