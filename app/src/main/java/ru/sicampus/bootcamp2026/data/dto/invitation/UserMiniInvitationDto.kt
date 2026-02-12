package ru.sicampus.bootcamp2026.data.dto.invitation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMiniInvitationDto(
    @SerialName("id") val id: Long,
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("photoUrl") val photoUrl: String,
    @SerialName("status") val status: String,
    @SerialName("respondedAt") val respondedAt: String?=null
)