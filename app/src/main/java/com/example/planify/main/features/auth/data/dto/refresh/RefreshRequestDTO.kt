package com.example.planify.main.features.auth.data.dto.refresh

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestDTO(
    @SerialName("refreshToken")
    val refreshToken: String
)
