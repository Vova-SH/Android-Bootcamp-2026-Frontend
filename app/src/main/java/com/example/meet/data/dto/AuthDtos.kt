package com.example.meet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Request: POST /api/auth/login */
@Serializable
data class LoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

/** Request: POST /api/auth/register */
@Serializable
data class RegisterRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("fullName") val fullName: String,
    @SerialName("position") val position: String? = null,
    @SerialName("department") val department: String? = null
)

/** Response: POST /api/auth/login */
@Serializable
data class JwtResponse(
    @SerialName("token") val token: String,
    @SerialName("id") val id: Long,
    @SerialName("email") val email: String,
    @SerialName("fullName") val fullName: String
)
