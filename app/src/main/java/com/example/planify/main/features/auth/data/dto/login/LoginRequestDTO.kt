package com.example.planify.main.features.auth.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String,

    @SerialName("clientName")
    val clientName: String
)
