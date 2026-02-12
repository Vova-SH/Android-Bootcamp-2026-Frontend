package com.example.planify.main.features.meetings.data.dto.reschedule_invite

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class InviteRescheduleRequestDTO(
    @Contextual
    @SerialName("rescheduleTo")
    val rescheduleTo: LocalDateTime
)