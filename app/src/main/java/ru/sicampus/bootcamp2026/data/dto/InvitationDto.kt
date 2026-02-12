package ru.sicampus.bootcamp2026.data.dto

data class InvitationDto(
    val id: Long? = null,
    val meetingId: Long? = null,
    val meetingStartAt: String? = null,
    val meetingEndAt: String? = null,
    val meetingType: MeetingTypeDto? = null,
    val inviteeId: Long? = null,
    val organizerId: Long? = null,
    val organizerName: String? = null,
    val organizerLastname: String? = null,
    val status: InvitationStatusDto? = null
)
