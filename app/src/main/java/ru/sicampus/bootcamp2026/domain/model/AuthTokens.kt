package ru.sicampus.bootcamp2026.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Модель токенов авторизации
 */
data class AuthTokens(
    val userId: UUID,
    val username: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: LocalDateTime,
    val refreshTokenExpiresAt: LocalDateTime
)

