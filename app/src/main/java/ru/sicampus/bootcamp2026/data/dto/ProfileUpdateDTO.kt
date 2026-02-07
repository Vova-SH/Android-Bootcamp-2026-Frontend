package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProfileUpdateDTO(
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("jobTitle")
    val jobTitle: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("avatarUrl")
    val avatarUrl: String?,
    @SerialName("newPassword")
    val newPassword: String? = null

)
