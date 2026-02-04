package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.dto.response.AuthResponseDto
import ru.sicampus.bootcamp2026.domain.model.AuthInfo

fun AuthResponseDto.toDomain(): AuthInfo = AuthInfo(
    userId = userId,
    email = email,
    username = username,
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn
)

