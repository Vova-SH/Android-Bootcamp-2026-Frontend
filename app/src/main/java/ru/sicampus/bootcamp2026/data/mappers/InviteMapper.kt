package ru.sicampus.bootcamp2026.data.mappers

import ru.sicampus.bootcamp2026.data.dto.InviteDto
import ru.sicampus.bootcamp2026.data.dto.InviteStatusDto
import ru.sicampus.bootcamp2026.domain.models.invite.Invite
import ru.sicampus.bootcamp2026.domain.models.invite.InviteStatus
import java.time.Instant

fun InviteDto.toDomain(): Invite? {
    return Invite(
        id = id ?: return null,
        meetId = meetId ?: return null,
        inviterUserId = inviterUserId ?: return null,
        invitedUserId = invitedUserId ?: return null,
        status = status?.toDomain() ?: return null,
        createdAt = Instant.parse(createdAt) ?: return null,
    )
}

fun Invite.toDto(): InviteDto = InviteDto(
    id = id,
    meetId = meetId,
    inviterUserId = inviterUserId,
    invitedUserId = invitedUserId,
    status = status.toDto(),
    createdAt = createdAt.toString()
)

fun InviteStatusDto.toDomain(): InviteStatus = when (this) {
    InviteStatusDto.PENDING -> InviteStatus.PENDING
    InviteStatusDto.ACCEPTED -> InviteStatus.ACCEPTED
    InviteStatusDto.DECLINED -> InviteStatus.DECLINED
}

fun InviteStatus.toDto(): InviteStatusDto = when (this) {
    InviteStatus.PENDING -> InviteStatusDto.PENDING
    InviteStatus.ACCEPTED -> InviteStatusDto.ACCEPTED
    InviteStatus.DECLINED -> InviteStatusDto.DECLINED
}