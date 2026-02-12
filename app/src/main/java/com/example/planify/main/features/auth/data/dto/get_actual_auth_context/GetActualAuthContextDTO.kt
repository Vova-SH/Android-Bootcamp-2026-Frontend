package com.example.planify.main.features.auth.data.dto.get_actual_auth_context

import com.example.planify.main.features.auth.data.dto.AuthContextDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetActualAuthContextDTO(
    @SerialName("context")
    val context: AuthContextDTO
)
