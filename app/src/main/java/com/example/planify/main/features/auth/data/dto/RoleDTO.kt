package com.example.planify.main.features.auth.data.dto

import com.example.planify.main.features.auth.domain.entities.Role
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class RoleDTO(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String
) {
    fun toEntity(): Role {
        return Role(
            id = id,
            name = name
        )
    }
}
