package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class InvitationCreateDto(
    val emailTarget: String,
    val meetingId: Long
)