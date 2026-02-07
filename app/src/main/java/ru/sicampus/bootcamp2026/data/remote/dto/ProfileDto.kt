package ru.sicampus.bootcamp2026.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTO для запроса обновления профиля
 */
@Serializable
data class UserProfileRequest(
    val username: String? = null,
    val avatarUrl: String? = null
)

/**
 * DTO для ответа с профилем пользователя
 */
@Serializable
data class UserProfileResponse(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null
)

/**
 * DTO для запроса обновления аватара
 */
@Serializable
data class UpdateAvatarRequest(
    val avatarUrl: String
)

/**
 * DTO для запроса сброса пароля
 */
@Serializable
data class ResetPasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)

