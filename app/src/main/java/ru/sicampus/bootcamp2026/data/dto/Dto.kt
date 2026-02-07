package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageDto<T>(
    @SerialName("content")
    val content: List<T>
)

@Serializable
data class RegistrationRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("fullName")
    val fullName: String
)

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("fullName")
    val fullName: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("pfpUrl")
    val pfpUrl: String? = null
)

@Serializable
data class InvitationDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("meetingId")
    val meetingId: Long? = null,
    @SerialName("inviteeId")
    val inviteeId: Long? = null,
    @SerialName("status")
    val status: Int? = null,
    @SerialName("invitedAt")
    val invitedAt: String? = null,
    @SerialName("respondedAt")
    val respondedAt: String? = null,
    @SerialName("meeting")
    val meeting: MeetingDto? = null
)

@Serializable
data class MeetingDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("organizerId")
    val organizerId: Long? = null,
    @SerialName("date")
    val date: String? = null,
    @SerialName("startHour")
    val startHour: Int? = null,
    @SerialName("createdAt")
    val createdAt: String? = null
)
