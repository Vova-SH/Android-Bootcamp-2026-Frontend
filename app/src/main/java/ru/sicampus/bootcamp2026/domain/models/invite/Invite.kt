package ru.sicampus.bootcamp2026.domain.models.invite

import java.time.Instant

data class Invite(
    val id: Int,
    val meetId: Int,
    val inviterUserId: Int,
    val invitedUserId: Int,
    val status: InviteStatus,
    val createdAt: Instant
)