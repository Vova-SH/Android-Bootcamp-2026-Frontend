package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    @SerialName("email")
    val email: String?,

    @SerialName("password")
    val password: String?,

    @SerialName("fullName")
    val fullName: String?
)