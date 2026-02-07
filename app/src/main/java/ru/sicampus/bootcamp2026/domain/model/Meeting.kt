package ru.sicampus.bootcamp2026.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Доменная модель участника встречи
 */
data class Participant(
    val userId: UUID,
    val username: String,
    val status: ParticipantStatus
)

/**
 * Доменная модель встречи
 */
data class Meeting(
    val id: UUID,
    val organizerId: UUID,
    val organizerUsername: String,
    val title: String,
    val description: String?,
    val location: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val status: MeetingStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val participants: List<Participant>
)

