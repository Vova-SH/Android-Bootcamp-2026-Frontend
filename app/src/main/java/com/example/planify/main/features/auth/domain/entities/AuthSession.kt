package com.example.planify.main.features.auth.domain.entities

import java.time.LocalDateTime

data class AuthSession(
    val uuid: String,
    val name: String,
    val userId: Long,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val lastUsedAt: LocalDateTime,
    val expiresAt: LocalDateTime
)
