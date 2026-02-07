package ru.sicampus.bootcamp2026.domain.model

data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?
)
