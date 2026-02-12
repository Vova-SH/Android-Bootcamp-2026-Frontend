package com.example.planify.main.features.auth.data.dto

import com.example.planify.main.features.auth.domain.entities.UserPrivate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserPrivateDTO(
    @SerialName("id")
    val id: Long,

    @SerialName("username")
    val username: String,

    @SerialName("email")
    val email: String
) {
    fun toEntity(): UserPrivate = UserPrivate(
        id = id,
        username = username,
        email = email
    )
}
