package com.example.planify.main.features.auth.data.dto

import com.example.planify.main.features.auth.domain.entities.Authority
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class AuthorityDTO(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String
) {
    fun toEntity(): Authority = Authority(
        id = id,
        name = name
    )
}
