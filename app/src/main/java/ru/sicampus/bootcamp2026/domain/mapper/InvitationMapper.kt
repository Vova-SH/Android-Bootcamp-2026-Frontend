package ru.sicampus.bootcamp2026.domain.mapper

import ru.sicampus.bootcamp2026.data.dto.invitation.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.invitation.UserMiniInvitationDto
import ru.sicampus.bootcamp2026.domain.entities.Invitation
import ru.sicampus.bootcamp2026.domain.entities.UserMiniInvitation
import java.time.LocalDateTime

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
        return UserMiniInvitation(
            id = dto.id,
            firstName = dto.firstName,
            secondName = dto.secondName,
            photoUrl = dto.photoUrl,
            status = dto.status,
            respondedAt = LocalDateTime.parse(dto.respondedAt),
        )
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
            date = domain.date,
            timeStart = domain.timeStart,
            timeEnd = domain.timeEnd
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
            date = dto.date,
            timeStart = dto.timeStart,
            timeEnd = dto.timeEnd
        )
    }
}