package ru.sicampus.bootcamp2026.domain.entities

import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    val id: Long,
    val email: String,
    val firstName: String,
    val secondName: String,
    val description: String? = null,
    val position: String? = null,
    val department: String? = null,
    val photoUrl: String,
    val role: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class UserMini(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val photoUrl: String
)

data class UserMiniInvitation(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val photoUrl: String,
    val status: String,
    val respondedAt: LocalDateTime
)

data class UserUpdate(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val description: String? = null,
    val position: String? = null,
    val department: String? = null
)