package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterDto(
    val firstName: String,
    val secondName: String,
    val patronymic: String? = null,
    val position: String? = null,
    val email: String,
    val password: String,
    val passwordAgain: String
)