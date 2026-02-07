package ru.sicampus.bootcamp2026.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("jobTitle")
    val jobTitle: String?,
    @SerialName("email")
    val email: String,
    @SerialName("avatarUrl")
    val avatarUrl: String?,

)