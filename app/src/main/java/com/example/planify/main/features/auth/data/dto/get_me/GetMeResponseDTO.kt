package com.example.planify.main.features.auth.data.dto.get_me

import com.example.planify.main.features.auth.data.dto.UserPrivateDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMeResponseDTO (
    @SerialName("user")
    val user: UserPrivateDTO
)