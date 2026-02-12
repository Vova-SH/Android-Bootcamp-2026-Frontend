package ru.sicampus.bootcamp2026.data.dto.meeting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
import ru.sicampus.bootcamp2026.data.dto.invitation.UserMiniInvitationDto

@Serializable
data class MeetingDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("address") val address: String,
    @SerialName("description") val description: String? = null,
    @SerialName("date") val date: String,
    @SerialName("timeStart") val timeStart: String,
    @SerialName("timeEnd") val timeEnd: String,
    @SerialName("organizer") val organizer: UserMiniDto,
    @SerialName("users") val users: List<UserMiniInvitationDto> = emptyList(),
    @SerialName("createAt") val createAt: String
)