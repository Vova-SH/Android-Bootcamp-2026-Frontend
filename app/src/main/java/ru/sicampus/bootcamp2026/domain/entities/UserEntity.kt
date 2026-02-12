package ru.sicampus.bootcamp2026.domain.entities

class UserEntity (
    val id: Long,
    val name: String,
    val lastName: String,
    val email: String,
    val login: String,
    val phoneNumber: String,
    val department: String?,
    val position: String?,
    val photoUrl: String?
)