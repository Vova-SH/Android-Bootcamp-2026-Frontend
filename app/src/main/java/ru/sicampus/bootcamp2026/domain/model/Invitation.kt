package ru.sicampus.bootcamp2026.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Доменная модель приглашения
 */
data class Invitation(
    val id: UUID,
    val meetingId: UUID,
    val meetingTitle: String,
    val meetingDescription: String?,
    val meetingLocation: String?,
    val meetingStartTime: LocalDateTime,
    val meetingEndTime: LocalDateTime,
    val organizerUsername: String,
    val status: ParticipantStatus,
    val createdAt: LocalDateTime
)

