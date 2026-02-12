package com.example.planify.main.features.meetings.data.dto

import com.example.planify.main.features.meetings.domain.entities.MeetingInvite
import com.example.planify.main.features.meetings.domain.entities.MeetingInviteStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MeetingInviteDTO(
    @SerialName("uuid")
    val uuid: String,

    @SerialName("meetingId")
    val meetingId: Long,

    @SerialName("senderId")
    val senderId: Long,

    @SerialName("targetId")
    val targetId: Long,

    @SerialName("status")
    val status: MeetingInviteStatus,

    @Contextual
    @SerialName("createdAt")
    val createdAt: LocalDateTime,

    @Contextual
    @SerialName("updatedAt")
    val updatedAt: LocalDateTime
) {
    fun toEntity(): MeetingInvite = MeetingInvite(
        uuid = uuid,
        meetingId = meetingId,
        senderId = senderId,
        targetId = targetId,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
