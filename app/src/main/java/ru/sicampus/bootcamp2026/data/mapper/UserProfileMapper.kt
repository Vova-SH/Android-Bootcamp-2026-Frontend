package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.dto.response.UserProfileResponseDto
import ru.sicampus.bootcamp2026.domain.model.UserProfile

fun UserProfileResponseDto.toDomain(): UserProfile = UserProfile(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl
)

