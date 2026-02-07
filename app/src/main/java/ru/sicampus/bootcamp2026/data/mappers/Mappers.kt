package ru.sicampus.bootcamp2026.data.mappers

import ru.sicampus.bootcamp2026.data.dto.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import java.time.LocalDate
import java.time.LocalDateTime

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id ?: -1L,
        name = fullName ?: "Unknown",
        email = email
    )
}

fun InvitationDto.toEntity(): InvitationEntity {
    return InvitationEntity(
        id = id ?: -1L,
        meetingId = meetingId ?: -1L,
        inviteeId = inviteeId ?: -1L,
        status = status ?: 0,
        invitedAt = invitedAt?.let { LocalDateTime.parse(it) },
        respondedAt = respondedAt?.let { LocalDateTime.parse(it) },
        meeting = meeting?.toEntity()
    )
}

fun MeetingDto.toEntity(): MeetingEntity {
    return MeetingEntity(
        id = id ?: -1L,
        title = title ?: "Untitled",
        organizerId = organizerId ?: -1L,
        date = date?.let { LocalDate.parse(it) } ?: LocalDate.now(),
        startHour = startHour ?: 0,
        createdAt = createdAt?.let { LocalDateTime.parse(it) }
    )
}

fun MeetingEntity.toDto(): MeetingDto {
    return MeetingDto(
        id = if (id == -1L) null else id,
        title = title,
        organizerId = organizerId,
        date = date.toString(),
        startHour = startHour,
        createdAt = createdAt?.toString()
    )
}

fun InvitationEntity.toDto(): InvitationDto {
    return InvitationDto(
        id = if (id == -1L) null else id,
        meetingId = meetingId,
        inviteeId = inviteeId,
        status = status,
        invitedAt = invitedAt?.toString(),
        respondedAt = respondedAt?.toString(),
        meeting = meeting?.toDto()
    )
}
