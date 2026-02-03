package ru.sicampus.bootcamp2026.domain.model

data class AuthInfo(
    val userId: String,
    val email: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
