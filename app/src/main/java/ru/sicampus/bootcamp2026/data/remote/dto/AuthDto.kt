package ru.sicampus.bootcamp2026.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTO для запроса регистрации
 */
@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

/**
 * DTO для запроса входа
 */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * DTO для ответа с токенами
 */
@Serializable
data class AuthResponse(
    val userId: String,
    val username: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: String,
    val refreshTokenExpiresAt: String
)

