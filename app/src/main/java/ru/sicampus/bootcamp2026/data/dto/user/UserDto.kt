package ru.sicampus.bootcamp2026.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserDto(
    @SerialName("id") val id: Long,
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("email") val email: String,
    @SerialName("description") val description: String? = null,
    @SerialName("position") val position: String? = null,
    @SerialName("department") val department: String? = null,
    @SerialName("photoUrl") val photoUrl: String,
    @SerialName("role") val role: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String
)