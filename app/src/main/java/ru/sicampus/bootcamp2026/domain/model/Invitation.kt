package ru.sicampus.bootcamp2026.domain.model

data class Invitation(
    val id: Long,
    val meetingId: Long,
    val status: InvitationStatus,
    val createdAt: String
)