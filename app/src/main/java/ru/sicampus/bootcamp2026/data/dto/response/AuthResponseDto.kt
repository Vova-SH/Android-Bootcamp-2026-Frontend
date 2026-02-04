package ru.sicampus.bootcamp2026.data.dto.response

data class AuthResponseDto(
    val userId: String,// UUID на сервере
    val email: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)