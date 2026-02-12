package com.example.planify.main.features.auth.data.dto

import com.example.planify.main.features.auth.domain.entities.AuthSession
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AuthSessionPrivateDTO(
    @SerialName("uuid")
    val uuid: String,

    @SerialName("name")
    val name: String,

    @SerialName("userId")
    val userId: Long,

    @SerialName("active")
    val isActive: Boolean = true,

    @Contextual
    @SerialName("createdAt")
    val createdAt: LocalDateTime,

    @Contextual
    @SerialName("lastUsedAt")
    val lastUsedAt: LocalDateTime,

    @Contextual
    @SerialName("expiresAt")
    val expiresAt: LocalDateTime
) {
    fun toEntity(): AuthSession = AuthSession(
        uuid = uuid,
        name = name,
        userId = userId,
        isActive = isActive,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt,
        expiresAt = expiresAt
    )
}
