package ru.sicampus.bootcamp2026.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTO для ответа с приглашением
 */
@Serializable
data class InvitationResponse(
    val id: String,
    val meetingId: String,
    val meetingTitle: String,
    val meetingDescription: String? = null,
    val meetingLocation: String? = null,
    val meetingStartTime: String,
    val meetingEndTime: String,
    val organizerUsername: String,
    val status: String,
    val createdAt: String
)

/**
 * DTO для запроса ответа на приглашение
 */
@Serializable
data class InvitationActionRequest(
    val status: String
)

