package com.example.planify.main.features.auth.data.dto

import com.example.planify.main.features.auth.domain.entities.AuthContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthContextDTO(
    @SerialName("session")
    val session: AuthSessionPrivateDTO,

    @SerialName("user")
    val user: UserPrivateDTO,

    @SerialName("accessInfo")
    val accessInfo: AccessInfoDTO
) {
    fun toEntity(): AuthContext = AuthContext(
        session = session.toEntity(),
        user = user.toEntity(),
        accessInfo = accessInfo.toEntity()
    )
}
