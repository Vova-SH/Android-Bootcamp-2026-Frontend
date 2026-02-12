package com.example.planify.main.features.auth.data.dto.register

import com.example.planify.main.features.auth.data.dto.AccessInfoDTO
import com.example.planify.main.features.auth.data.dto.AuthSessionPrivateDTO
import com.example.planify.main.features.auth.data.dto.AuthTokenPairDTO
import com.example.planify.main.features.auth.data.dto.UserPrivateDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDTO(
    @SerialName("user")
    val user: UserPrivateDTO,

    @SerialName("session")
    val session: AuthSessionPrivateDTO,

    @SerialName("tokens")
    val tokens: AuthTokenPairDTO,

    @SerialName("accessInfo")
    val accessInfo: AccessInfoDTO
)
