package com.example.planify.main.features.meetings.data.dto.reschedule_invite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InviteRescheduleResponseDTO (
    @SerialName("shouldReschedule")
    val shouldReschedule: Boolean
)