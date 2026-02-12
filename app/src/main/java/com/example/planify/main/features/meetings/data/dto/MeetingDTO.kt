package com.example.planify.main.features.meetings.data.dto

import com.example.planify.main.features.meetings.domain.entities.Meeting
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MeetingDTO(
    @SerialName("id")
    val id: Long,

    @SerialName("ownerId")
    val ownerId: Long,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String,

    @SerialName("location")
    val location: String,

    @Contextual
    @SerialName("startsAt")
    val startsAt: LocalDateTime,

    @SerialName("duration")
    val duration: Int
) {
    fun toEntity(): Meeting = Meeting(
        id = id,
        ownerId = ownerId,
        name = name,
        description = description,
        location = location,
        startsAt = startsAt,
        duration = duration
    )
}
