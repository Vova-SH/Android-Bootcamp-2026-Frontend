package ru.sicampus.bootcamp2026.android.data.dto

import kotlinx.serialization.Serializable

// класс для статуса встречи
@Serializable
enum class InvitationStatusDto { PENDING, ACCEPTED, DECLINED }

@Serializable
data class InvitationDTO(
    val id: Long,
    val meetingId: Long,
    val inviteeId: Long,
    val status: InvitationStatusDto, // "PENDING", "ACCEPTED", "DECLINED"
    val respondedAt: String? = null,
    val createdAt: String
)