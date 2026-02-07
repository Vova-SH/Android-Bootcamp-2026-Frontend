package com.teto.planner.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class CreateUserRequest(
    val login: String,
    val name: String,
    val password: String,
    val telegramNick: String? = null,
    val bio: String? = null
)

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val telegramNick: String? = null,
    val bio: String? = null
)