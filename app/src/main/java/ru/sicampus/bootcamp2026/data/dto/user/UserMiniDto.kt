package ru.sicampus.bootcamp2026.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMiniDto(
    @SerialName("id") val id: Long,
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("photoUrl") val photoUrl: String
)