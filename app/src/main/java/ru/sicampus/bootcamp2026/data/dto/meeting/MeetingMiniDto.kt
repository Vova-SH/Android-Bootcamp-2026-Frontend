package ru.sicampus.bootcamp2026.data.dto.meeting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class MeetingMiniDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("address") val address: String,
    @SerialName("date") val date: String,
    @SerialName("timeStart") val timeStart: String,
    @SerialName("timeEnd") val timeEnd: String,
    @SerialName("organizer") val organizer: UserMiniDto
)