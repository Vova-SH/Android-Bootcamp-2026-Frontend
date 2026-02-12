package ru.sicampus.bootcamp2026.domain.mapper

import ru.sicampus.bootcamp2026.data.dto.invitation.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.invitation.UserMiniInvitationDto
import ru.sicampus.bootcamp2026.domain.entities.Invitation
import ru.sicampus.bootcamp2026.domain.entities.UserMiniInvitation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object InvitationMapper {

    fun toDto(domain: UserMiniInvitation): UserMiniInvitationDto {
        return UserMiniInvitationDto(
            id = domain.id,
            firstName = domain.firstName,
            secondName = domain.secondName,
            photoUrl = domain.photoUrl,
            status = domain.status,
            respondedAt = domain.respondedAt.toString(),
        )
    }

    fun toDomain(dto: UserMiniInvitationDto): UserMiniInvitation {
        if (dto.respondedAt == null){
            return UserMiniInvitation(
                id = dto.id,
                firstName = dto.firstName,
                secondName = dto.secondName,
                photoUrl = dto.photoUrl,
                status = dto.status,
                respondedAt = null
            )
            }else{
            return UserMiniInvitation(
                id = dto.id,
                firstName = dto.firstName,
                secondName = dto.secondName,
                photoUrl = dto.photoUrl,
                status = dto.status,
                respondedAt = LocalDateTime.parse(dto.respondedAt)
            )
        }
    }

    fun toDto(domain: Invitation): InvitationDto {
        return InvitationDto(
            id = domain.id,
            authorId = domain.authorId,
            authorFirstName = domain.authorFirstName,
            authorSecondName = domain.authorSecondName,
            meetingId = domain.meetingId,
            title = domain.title,
            address = domain.address,
            date = domain.date.toString(),
            timeStart = domain.timeStart.toString(),
            timeEnd = domain.timeEnd.toString()
        )
    }

    fun toDomain(dto: InvitationDto): Invitation {
        return Invitation(
            id = dto.id,
            authorId = dto.authorId,
            authorFirstName = dto.authorFirstName,
            authorSecondName = dto.authorSecondName,
            meetingId = dto.meetingId,
            title = dto.title,
            address = dto.address,
            date = LocalDate.parse(dto.date),
            timeStart = LocalTime.parse(dto.timeStart),
            timeEnd = LocalTime.parse(dto.timeEnd)
        )
    }
}