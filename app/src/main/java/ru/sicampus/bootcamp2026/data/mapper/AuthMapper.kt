package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.remote.dto.AuthResponse
import ru.sicampus.bootcamp2026.domain.model.AuthTokens
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Маппер для преобразования AuthResponse в AuthTokens
 */
fun AuthResponse.toDomain(): AuthTokens {
    return AuthTokens(
        userId = UUID.fromString(userId),
        username = username,
        email = email,
        accessToken = accessToken,
        refreshToken = refreshToken,
        accessTokenExpiresAt = LocalDateTime.parse(accessTokenExpiresAt, DateTimeFormatter.ISO_DATE_TIME),
        refreshTokenExpiresAt = LocalDateTime.parse(refreshTokenExpiresAt, DateTimeFormatter.ISO_DATE_TIME)
    )
}

