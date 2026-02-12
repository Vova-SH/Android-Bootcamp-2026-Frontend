package com.example.planify.main.features.meetings.domain.schemas.actions

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MeetingActionNewParticipantSchema(
    @SerialName("meetingId")
    val meetingId: Long,

    @SerialName("newParticipantId")
    val newParticipantId: Long,

    @Contextual
    @SerialName("joinedAt")
    val joinedAt: LocalDateTime
)
