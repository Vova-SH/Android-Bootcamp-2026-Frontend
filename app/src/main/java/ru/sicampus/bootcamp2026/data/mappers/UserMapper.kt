package ru.sicampus.bootcamp2026.data.mappers

import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.models.User

fun UserDto.toDomain(): User? {
    return User(
        id = id ?: return null,
        email = email ?: return null,
        firstName = firstName ?: return null,
        lastName = lastName ?: return null,
        photoUrl = photoUrl, // не nonNull
    )
}

fun User.toDto(): UserDto = UserDto(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    photoUrl = photoUrl
)