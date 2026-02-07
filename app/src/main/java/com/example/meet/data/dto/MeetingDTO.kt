package com.example.meet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("organizerId") val organizerId: Long,
    @SerialName("roomId") val roomId: Long? = null,
    @SerialName("startTime") val startTime: String,
    @SerialName("endTime") val endTime: String,
    @SerialName("recurrencePattern") val recurrencePattern: String? = null,
    @SerialName("recurrenceEndDate") val recurrenceEndDate: String? = null,
    @SerialName("meetingPriority") val meetingPriority: String = "MEDIUM",
    @SerialName("status") val status: String = "SCHEDULED",
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("participantIds") val participantIds: List<Long>? = null,
    @SerialName("location") val location: String? = null,
)

@Serializable
data class MeetingPageDto(
    @SerialName("content") val content: List<MeetingDto>
)

@Serializable
data class CreateMeetingDto(
    val title: String,
    val description: String? = null,
    val organizerId: Long,
    val participantIds: List<Long> = emptyList(),
    val roomId: Long? = null,
    val startTime: String,
    val endTime: String,
    val meetingPriority: String,
    val status: String,
    val recurrencePattern: String? = null,
    val recurrenceEndDate: String? = null,
    val location: String? = null
)