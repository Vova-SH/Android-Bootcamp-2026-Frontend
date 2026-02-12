package com.example.planify.main.features.meetings.data.dto.create_invite

import com.example.planify.main.features.meetings.data.dto.MeetingInviteDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class InviteUserResponseDTO (
    @SerialName("invite")
    val invite: MeetingInviteDTO
)