package com.example.planify.main.features.auth.data.dto

import com.example.planify.main.features.auth.domain.entities.AuthTokenPair
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenPairDTO(
    @SerialName("accessToken")
    val accessToken: String,

    @SerialName("refreshToken")
    val refreshToken: String
) {
    fun toEntity(): AuthTokenPair = AuthTokenPair(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}
