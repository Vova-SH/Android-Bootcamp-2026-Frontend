package ru.sicampus.bootcamp2026.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateDto(
    @SerialName("id") val id: Long,
    @SerialName("firstName") val firstName: String,
    @SerialName("secondName") val secondName: String,
    @SerialName("description") val description: String? = null,
    @SerialName("position") val position: String? = null,
    @SerialName("department") val department: String? = null
)