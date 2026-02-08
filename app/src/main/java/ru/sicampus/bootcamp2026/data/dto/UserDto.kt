package ru.sicampus.bootcamp2026.data.dto

data class UserDto(
    val id: Long? = null,
    val login: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val aboutMe: String? = null,
    val position: String? = null,
    val photoUrl: String? = null
)
