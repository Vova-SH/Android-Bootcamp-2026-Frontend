package ru.sicampus.bootcamp2026.data.mapper

import ru.sicampus.bootcamp2026.data.remote.dto.PageResponse
import ru.sicampus.bootcamp2026.data.remote.dto.UserProfileResponse
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.model.User
import java.util.UUID

/**
 * Маппер для преобразования UserProfileResponse в User
 */
fun UserProfileResponse.toDomain(): User {
    return User(
        id = UUID.fromString(id),
        username = username,
        email = email,
        avatarUrl = avatarUrl
    )
}

/**
 * Маппер для преобразования PageResponse<UserProfileResponse> в PaginatedData<User>
 */
fun PageResponse<UserProfileResponse>.toDomain(): PaginatedData<User> {
    return PaginatedData(
        content = content.map { it.toDomain() },
        totalPages = totalPages,
        totalElements = totalElements,
        number = number,
        size = size,
        first = first,
        last = last,
        empty = empty
    )
}

