package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto (
    @SerialName("email")
    val email: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("company")
    val company: String? = null,
    @SerialName("name")
    val name: String? = null
)