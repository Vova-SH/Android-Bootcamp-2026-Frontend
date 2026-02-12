package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswerInvitationRequestDTO(
    @SerialName("status") val status: String,
    @SerialName("id") val id: Int,

)
