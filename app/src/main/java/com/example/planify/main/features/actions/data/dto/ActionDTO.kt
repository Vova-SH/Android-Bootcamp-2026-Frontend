package com.example.planify.main.features.actions.data.dto

import com.example.planify.main.features.actions.domain.entities.Action
import com.example.planify.main.features.actions.domain.utils.ActionDataParser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ActionDTO(
    @SerialName("id")
    val id: String,

    @SerialName("type")
    val type: String,

    @SerialName("data")
    val data: JsonElement?
) {
    fun toEntity(actionDataParser: ActionDataParser): Action<*> = Action(
        id = id,
        type = type,
        data = data?.let { actionDataParser.deserialize(data, type) }
    )
}
