package ru.sicampus.bootcamp2026.android.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Long,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val department: String,
    val position: String,
    val createdAt: String
)