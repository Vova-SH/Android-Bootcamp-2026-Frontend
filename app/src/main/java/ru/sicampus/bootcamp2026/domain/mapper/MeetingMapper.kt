package ru.sicampus.bootcamp2026.domain.mapper

import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingCreateDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto
import ru.sicampus.bootcamp2026.domain.entities.Meeting
import ru.sicampus.bootcamp2026.domain.entities.MeetingCreate
import ru.sicampus.bootcamp2026.domain.entities.MeetingMini
import java.time.LocalDate
import java.time.LocalTime

object MeetingMapper {
//
//    fun toDto(domain: Meeting): MeetingDto {
//        return MeetingDto(
//            id = domain.id,
//            title = domain.title,
//            address = domain.address,
//            description = domain.description,
//            date = domain.date,
//            timeStart = domain.timeStart,
//            timeEnd = domain.timeEnd,
//            organizer = UserMapper.toDto(domain.organizer),
//            users = domain.users.map { InvitationMapper.toDto(it) },
//            createAt = domain.createdAt
//        )
//    }

//    fun toDomain(dto: MeetingDto): Meeting {
//        return Meeting(
//            id = dto.id,
//            title = dto.title,
//            address = dto.address,
//            description = dto.description,
//            date = LocalDate.parse(dto.date),
//            timeStart = LocalTime.parse(dto.timeStart),
//            timeEnd = LocalTime.parse(dto.timeEnd),
//            organizer = UserMapper.toDomain(dto.organizer),
//            users = dto.users.map { InvitationMapper.toDomain(it) },
//            createdAt = dto.createAt
//        )
//    }
//
//    fun toDomain(domain: MeetingMiniDto): MeetingMini {
//        return MeetingMini(
//            id = domain.id,
//            title = domain.title,
//            description = domain.description,
//            address = domain.address,
//            date = domain.date,
//            timeStart = domain.timeStart,
//            timeEnd = domain.timeEnd,
//            organizer = UserMapper.toDomain(domain.organizer)
//        )
//    }
//
//    fun toDto(domain: MeetingCreate): MeetingCreateDto {
//        return MeetingCreateDto(
//            title = domain.title,
//            address = domain.address,
//            description = domain.description,
//            date = domain.date,
//            timeStart = domain.timeStart,
//            timeEnd = domain.timeEnd,
//            usersId = domain.userId
//        )
//    }
//
//    fun toDomain(dto: MeetingCreateDto): MeetingCreate {
//        return MeetingCreate(
//            title = dto.title,
//            address = dto.address,
//            description = dto.description,
//            date = dto.date,
//            timeStart = dto.timeStart,
//            timeEnd = dto.timeEnd,
//            userId = dto.usersId
//        )
//    }
}