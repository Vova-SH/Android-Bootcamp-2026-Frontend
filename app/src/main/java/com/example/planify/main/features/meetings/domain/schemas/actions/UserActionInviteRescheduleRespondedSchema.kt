package com.example.planify.main.features.meetings.domain.schemas.actions

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserActionInviteRescheduleRespondedSchema(
    @SerialName("meetingId")
    val meetingId: Long,

    @SerialName("senderId")
    val senderId: Long,

    @SerialName("targetId")
    val targetId: Long,

    @SerialName("inviteUuid")
    val inviteUuid: String,

    @Contextual
    @SerialName("updatedAt")
    val updatedAt: LocalDateTime,

    @SerialName("shouldReschedule")
    val shouldReschedule: Boolean
)
