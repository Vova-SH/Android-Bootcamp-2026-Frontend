package ru.sicampus.bootcamp2026.data

import kotlinx.serialization.SerialName

data class RegisterResponse(
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