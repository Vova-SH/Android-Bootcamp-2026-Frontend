package com.example.planify.main.features.actions.data.dto.get_my_incoming_actions

import com.example.planify.main.features.actions.data.dto.ActionDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyIncomingActionsDTO(
    @SerialName("actions")
    val actions: List<ActionDTO>
)
