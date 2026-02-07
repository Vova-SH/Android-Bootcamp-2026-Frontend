package ru.sicampus.bootcamp2026.domain.model

import java.util.UUID

/**
 * Доменная модель пользователя
 */
data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val avatarUrl: String?
)

