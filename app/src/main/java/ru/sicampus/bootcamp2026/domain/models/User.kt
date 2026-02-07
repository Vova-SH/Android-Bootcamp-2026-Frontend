package ru.sicampus.bootcamp2026.domain.models

data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val photoUrl: String? = null,
)



