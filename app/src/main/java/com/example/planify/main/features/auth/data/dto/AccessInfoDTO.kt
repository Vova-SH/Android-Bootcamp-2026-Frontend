package com.example.planify.main.features.auth.data.dto

import com.example.planify.main.features.auth.domain.entities.AccessInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessInfoDTO(
    @SerialName("authorities")
    val authorities: List<AuthorityDTO> = emptyList(),

    @SerialName("roles")
    val roles: List<RoleDTO> = emptyList()
) {
    fun toEntity(): AccessInfo = AccessInfo(
        authorities = authorities.map { it.toEntity() },
        roles = roles.map { it.toEntity() }
    )
}
