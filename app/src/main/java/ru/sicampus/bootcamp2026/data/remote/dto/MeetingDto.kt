package ru.sicampus.bootcamp2026.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTO для участника встречи
 */
@Serializable
data class ParticipantResponse(
    val userId: String,
    val username: String,
    val status: String
)

/**
 * DTO для ответа со встречей
 */
@Serializable
data class MeetingResponse(
    val id: String,
    val organizerId: String,
    val organizerUsername: String,
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startTime: String,
    val endTime: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val participants: List<ParticipantResponse>
)

/**
 * DTO для запроса создания встречи
 */
@Serializable
data class CreateMeetingRequest(
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startTime: String,
    val endTime: String,
    val participantIds: List<String>
)

/**
 * DTO для запроса свободного времени
 */
@Serializable
data class FreeTimeRequest(
    val userIds: List<String>
)

/**
 * DTO для слота свободного времени
 */
@Serializable
data class FreeTimeSlotDto(
    val startTime: String,
    val endTime: String
)

/**
 * DTO для ответа со свободным временем
 */
@Serializable
data class FreeTimeResponse(
    val startEndTime: List<FreeTimeSlotDto>
)

