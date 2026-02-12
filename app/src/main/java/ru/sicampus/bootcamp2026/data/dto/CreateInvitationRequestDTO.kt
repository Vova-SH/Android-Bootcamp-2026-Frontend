package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateInvitationRequestDTO(
    @SerialName ("meetingId") val meetingId: Int,
    @SerialName ("employeeUsernames") val employeeUsernames: List<String>,
    @SerialName ("message") val message: String
)
