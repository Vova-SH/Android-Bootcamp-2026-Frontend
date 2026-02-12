package com.example.planify

import android.app.Application
import com.example.planify.main.features.actions.domain.utils.ActionDataParser
import com.example.planify.main.features.actions.domain.utils.registerSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.MeetingActionInviteRescheduleRequestedSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.MeetingActionInviteRescheduleRespondedSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.MeetingActionInviteStatusUpdatedSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.MeetingActionNewParticipantSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.MeetingActionUserInvitedSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.UserActionInviteRescheduleRequestedSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.UserActionInviteRescheduleRespondedSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.UserActionInviteStatusUpdatedSchema
import com.example.planify.main.features.meetings.domain.schemas.actions.UserActionInvitedToMeetingSchema
import com.google.crypto.tink.config.TinkConfig
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class PlanifyApplication : Application() {
    @Inject
    lateinit var actionDataParser: ActionDataParser

    override fun onCreate() {
        super.onCreate()

        initTink()
        initActionDataParser()
    }

    private fun initTink() {
        TinkConfig.register()
    }

    private fun initActionDataParser() {
        // User actions
        actionDataParser.registerSchema<UserActionInvitedToMeetingSchema>("meetings:invited")
        actionDataParser.registerSchema<UserActionInviteRescheduleRequestedSchema>("meetings:invite_reschedule_requested")
        actionDataParser.registerSchema<UserActionInviteRescheduleRespondedSchema>("meetings:invite_reschedule_responded")
        actionDataParser.registerSchema<UserActionInviteStatusUpdatedSchema>("meetings:invite_status_updated")

        // Meeting actions
        actionDataParser.registerSchema<MeetingActionInviteRescheduleRequestedSchema>("meetings:meeting:invite_reschedule_requested")
        actionDataParser.registerSchema<MeetingActionInviteRescheduleRespondedSchema>("meetings:meeting:invite_reschedule_responded")
        actionDataParser.registerSchema<MeetingActionInviteStatusUpdatedSchema>("meetings:meeting:invite_status_updated")
        actionDataParser.registerSchema<MeetingActionNewParticipantSchema>("meetings:meeting:new_participant")
        actionDataParser.registerSchema<MeetingActionUserInvitedSchema>("meetings:meeting:invited")
    }
}

// UI -> ViewModel -> Service (UseCases) -> Repository -> DataSource 💀💀💀
