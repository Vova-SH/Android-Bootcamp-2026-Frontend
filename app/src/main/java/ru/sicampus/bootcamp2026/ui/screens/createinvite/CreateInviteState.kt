package ru.sicampus.bootcamp2026.ui.screens.createinvite

import ru.sicampus.bootcamp2026.domain.entities.CreateMeetingEntity
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import ru.sicampus.bootcamp2026.ui.screens.invites.InvitesState

sealed interface CreateInviteState {
    data class Error(val reason: String) : CreateInviteState
    data object Loading : CreateInviteState
    data class Content(
        val invitation: CreateMeetingEntity
    ) : CreateInviteState
}