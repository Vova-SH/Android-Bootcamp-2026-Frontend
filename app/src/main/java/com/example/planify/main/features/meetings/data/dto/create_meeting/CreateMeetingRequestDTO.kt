package com.example.planify.main.features.meetings.data.dto.create_meeting

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CreateMeetingRequestDTO(
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
)
