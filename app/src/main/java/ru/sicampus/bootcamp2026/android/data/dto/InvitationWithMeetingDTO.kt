package ru.sicampus.bootcamp2026.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationWithMeetingDto(
    val id: Long,
    val inviteeId: Long,
    val status: InvitationStatusDto,
    val respondedAt: String? = null,
    val createdAt: String,
    val meeting: MeetingResponse
)

@Serializable
data class InvitationRespondRequest(
    val status: String
)
