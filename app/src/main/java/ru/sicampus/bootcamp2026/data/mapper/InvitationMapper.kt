package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.remote.dto.InvitationResponse
import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.ParticipantStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Маппер для преобразования InvitationResponse в Invitation
 */
fun InvitationResponse.toDomain(): Invitation {
    return Invitation(
        id = UUID.fromString(id),
        meetingId = UUID.fromString(meetingId),
        meetingTitle = meetingTitle,
        meetingDescription = meetingDescription,
        meetingLocation = meetingLocation,
        meetingStartTime = LocalDateTime.parse(meetingStartTime, DateTimeFormatter.ISO_DATE_TIME),
        meetingEndTime = LocalDateTime.parse(meetingEndTime, DateTimeFormatter.ISO_DATE_TIME),
        organizerUsername = organizerUsername,
        status = ParticipantStatus.valueOf(status),
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
    )
}

