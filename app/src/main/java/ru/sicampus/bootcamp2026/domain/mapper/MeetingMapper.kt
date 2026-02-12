package ru.sicampus.bootcamp2026.domain.mapper

import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingCreateDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto
import ru.sicampus.bootcamp2026.domain.entities.Meeting
import ru.sicampus.bootcamp2026.domain.entities.MeetingCreate
import ru.sicampus.bootcamp2026.domain.entities.MeetingMini
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object MeetingMapper {

    fun toDto(domain: Meeting): MeetingDto {
        return MeetingDto(
            id = domain.id,
            title = domain.title,
            address = domain.address,
            description = domain.description,
            date = domain.date.toString(),
            timeStart = domain.timeStart.toString(),
            timeEnd = domain.timeEnd.toString(),
            organizer = UserMapper.toDto(domain.organizer),
            users = domain.participants.map { InvitationMapper.toDto(it) },
            createAt = domain.createdAt.toString()
        )
    }

    fun toDomain(dto: MeetingDto): Meeting {
        return Meeting(
            id = dto.id,
            title = dto.title,
            address = dto.address,
            description = dto.description,
            date = LocalDate.parse(dto.date),
            timeStart = LocalTime.parse(dto.timeStart),
            timeEnd = LocalTime.parse(dto.timeEnd),
            organizer = UserMapper.toDomain(dto.organizer),
            participants = dto.users.map { InvitationMapper.toDomain(it) },
            createdAt = LocalDateTime.parse(dto.createAt)
        )
    }

    fun toDomain(dto: MeetingMiniDto): MeetingMini {
        return MeetingMini(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            address = dto.address,
            date = LocalDate.parse(dto.date),
            timeStart = LocalTime.parse(dto.timeStart),
            timeEnd = LocalTime.parse(dto.timeEnd),
            organizer = UserMapper.toDomain(dto.organizer)
        )
    }

    fun toDto(domain: MeetingCreate): MeetingCreateDto {
        return MeetingCreateDto(
            title = domain.title,
            address = domain.address,
            description = domain.description,
            date = domain.date,
            timeStart = domain.timeStart.toString(),
            timeEnd = domain.timeEnd.toString(),
            usersId = domain.participantIds
        )
    }

    fun toDomain(dto: MeetingCreateDto): MeetingCreate {
        return MeetingCreate(
            title = dto.title,
            description = dto.description,
            address = dto.address,
            date = dto.date,
            timeStart = LocalDateTime.parse(dto.timeStart),
            timeEnd = LocalDateTime.parse(dto.timeEnd),
            organizerId = 0, // Будет заполнено при создании
            participantIds = dto.usersId
        )
    }
}