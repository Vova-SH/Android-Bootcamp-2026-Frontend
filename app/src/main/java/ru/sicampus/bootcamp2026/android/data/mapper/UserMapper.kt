package ru.sicampus.bootcamp2026.android.data.mapper

import ru.sicampus.bootcamp2026.android.data.dto.UserDTO
import ru.sicampus.bootcamp2026.android.domain.entities.UserEntity

fun UserDTO.toEntity(): UserEntity =
    UserEntity(
        name = fullName,
        email = email
    )
