package ru.sicampus.bootcamp2026.domain.entities

import java.time.LocalDateTime

data class InvitationEntity(
    val id: Long,
    val meetingId: Long,
    val inviteeId: Long,
    val status: Int,
    val invitedAt: LocalDateTime?,
    val respondedAt: LocalDateTime?,
    val meeting: MeetingEntity? = null
)
