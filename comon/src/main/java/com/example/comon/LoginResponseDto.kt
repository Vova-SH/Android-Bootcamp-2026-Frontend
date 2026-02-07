package com.example.comon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("accessToken")
    val accessToken: String
)

