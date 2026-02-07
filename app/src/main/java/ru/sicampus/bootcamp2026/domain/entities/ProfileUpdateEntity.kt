package ru.sicampus.bootcamp2026.domain.entities

data class ProfileUpdateEntity(
    val name: String,
    val position: String,
    val email: String,
    val phoneNumber: String,
)
