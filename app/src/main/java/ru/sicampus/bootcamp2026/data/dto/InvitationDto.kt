package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationDto(
    val id: Long,
    val targetEmail: String,
    val meetingId: Long,
    val status: String,
    val createdAt: String
)