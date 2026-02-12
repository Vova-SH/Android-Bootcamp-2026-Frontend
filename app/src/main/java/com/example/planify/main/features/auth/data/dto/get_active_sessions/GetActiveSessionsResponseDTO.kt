package com.example.planify.main.features.auth.data.dto.get_active_sessions

import com.example.planify.main.features.auth.data.dto.AuthSessionPrivateDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetActiveSessionsResponseDTO(
    @SerialName("sessions")
    val sessions: List<AuthSessionPrivateDTO>
)
