package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val login: String,
    val password: String
)

@Serializable
data class RegisterRequestDto(
    val login: String,
    val password: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val department: String? = null,
    val position: String? = null,
    val photoUrl: String? = null
)