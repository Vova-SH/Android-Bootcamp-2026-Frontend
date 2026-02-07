package com.example.meet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    @SerialName("id") val id: Long,
    @SerialName("userId") val userId: Long? = null,
    @SerialName("meetingId") val meetingId: Long? = null,
    @SerialName("type") val type: String,
    @SerialName("title") val title: String,
    @SerialName("message") val message: String,
    @SerialName("read") val isRead: Boolean,
    @SerialName("createdAt") val createdAt: String,
)

@Serializable
data class NotificationPageDto(
    @SerialName("content") val content: List<NotificationDto>
)
