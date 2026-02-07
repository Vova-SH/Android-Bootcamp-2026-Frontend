package ru.sicampus.bootcamp2026.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterDTO(
    @SerialName("fullName")
    val fullName: String,
    @SerialName("jobTitle")
    val jobTitle: List<String>,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("passwordConfirm")
    val passwordConfirm: String,
    @SerialName("department")
    val department: List<String>
)