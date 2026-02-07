package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val patronymic: String? = null,
    val email: String,
    val position: String? = null
)