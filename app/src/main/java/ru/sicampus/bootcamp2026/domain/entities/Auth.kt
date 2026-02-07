package ru.sicampus.bootcamp2026.domain.entities

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val secondName: String
)

data class JwtResponse(
    val accessToken: String,
    val refreshToken: String
)