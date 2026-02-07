package ru.sicampus.bootcamp2026.domain.entities

data class UserEntity(
    val id: Long,
    val name: String,
    val email: String? = null
)