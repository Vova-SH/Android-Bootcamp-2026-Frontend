package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto (
    @SerialName("email")
    val email: String?,
    @SerialName("password")
    val password: String?,
    @SerialName("company")
    val company: String?,
    @SerialName("name")
    val name: String?
)