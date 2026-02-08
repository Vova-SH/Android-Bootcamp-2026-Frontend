package com.example.planify.main.navigation.screens.inbox_screen

import com.example.planify.main.features.meetings.domain.entities.MeetingContext

sealed interface InboxAction {
    data class Invite(
        val meetingContext: MeetingContext,
        val inviteUuid: String,
        val actionId: String
    ) : InboxAction
}