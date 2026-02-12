package com.example.planify.main.features.meetings.data.dto.get_invite

import com.example.planify.main.features.meetings.data.dto.MeetingInviteDTO
import com.example.planify.main.features.meetings.domain.entities.MeetingInvite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetInviteDTO (
    @SerialName("invite")
    val invite: MeetingInviteDTO
)