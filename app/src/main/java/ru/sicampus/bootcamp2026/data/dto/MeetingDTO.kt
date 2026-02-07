package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

@Serializable
data class MeetingDTO(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("description") val description: String?,
    @SerialName("startTime") val startTime: String?,
    @SerialName("ownerName") val ownerName: String?
)
fun MeetingDTO.toEntity(): MeetingEntity {
    val parsedStartTime = try {
        val dateTime = java.time.ZonedDateTime.parse(this.startTime ?: "").toLocalDateTime()
        val outputFormatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM HH:mm")
        dateTime.format(outputFormatter)
    } catch (_: Exception) {
        this.startTime ?: "Date parsing error"
    }

    return MeetingEntity(
        id = this.id ?: 0,
        name = this.name ?: "",
        description = this.description ?: "",
        startTime = parsedStartTime,
        ownerName = this.ownerName ?: ""
    )
}