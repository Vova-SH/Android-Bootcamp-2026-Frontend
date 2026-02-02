package ru.sicampus.bootcamp2026.ui.screens.invites

import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

sealed interface InvitesState {
    data class Error(val reason: String) : InvitesState
    data object Loading : InvitesState
    data class Content(
        val meeting: List<MeetingEntity>
    ) : InvitesState
}
