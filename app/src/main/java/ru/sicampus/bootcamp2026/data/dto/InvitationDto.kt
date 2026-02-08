package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationDto(
    val id: Long,
    val meetingId : Long,
    val personName: String,
    val status: String,
    val respondedAt: String? = null,
    val createdAt: String? = null,
)