package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.remote.dto.FreeTimeSlotDto
import ru.sicampus.bootcamp2026.data.remote.dto.MeetingResponse
import ru.sicampus.bootcamp2026.data.remote.dto.PageResponse
import ru.sicampus.bootcamp2026.data.remote.dto.ParticipantResponse
import ru.sicampus.bootcamp2026.domain.model.FreeTimeSlot
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.model.MeetingStatus
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.model.Participant
import ru.sicampus.bootcamp2026.domain.model.ParticipantStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Маппер для преобразования ParticipantResponse в Participant
 */
fun ParticipantResponse.toDomain(): Participant {
    return Participant(
        userId = UUID.fromString(userId),
        username = username,
        status = ParticipantStatus.valueOf(status)
    )
}

/**
 * Маппер для преобразования MeetingResponse в Meeting
 */
fun MeetingResponse.toDomain(): Meeting {
    return Meeting(
        id = UUID.fromString(id),
        organizerId = UUID.fromString(organizerId),
        organizerUsername = organizerUsername,
        title = title,
        description = description,
        location = location,
        startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME),
        endTime = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_DATE_TIME),
        status = MeetingStatus.valueOf(status),
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME),
        updatedAt = LocalDateTime.parse(updatedAt, DateTimeFormatter.ISO_DATE_TIME),
        participants = participants.map { it.toDomain() }
    )
}

/**
 * Маппер для преобразования PageResponse<MeetingResponse> в PaginatedData<Meeting>
 */
fun PageResponse<MeetingResponse>.toDomain(): PaginatedData<Meeting> {
    return PaginatedData(
        content = content.map { it.toDomain() },
        totalPages = totalPages,
        totalElements = totalElements,
        number = number,
        size = size,
        first = first,
        last = last,
        empty = empty
    )
}

/**
 * Маппер для преобразования FreeTimeSlotDto в FreeTimeSlot
 */
fun FreeTimeSlotDto.toDomain(): FreeTimeSlot {
    return FreeTimeSlot(
        startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME),
        endTime = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_DATE_TIME)
    )
}

