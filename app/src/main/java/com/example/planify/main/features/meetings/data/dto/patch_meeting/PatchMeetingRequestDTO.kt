package com.example.planify.main.features.meetings.data.dto.patch_meeting

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PatchMeetingRequestDTO(
    @SerialName("name")
    val name: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("location")
    val location: String? = null,

    @Contextual
    @SerialName("startsAt")
    val startsAt: LocalDateTime? = null,

    @SerialName("duration")
    val duration: Int? = null
)
