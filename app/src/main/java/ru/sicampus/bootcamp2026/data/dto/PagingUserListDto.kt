package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingUserListDto (
    @SerialName("content")
    val content: List<UserDto>? = null,
    @SerialName("last")
    val last: Boolean? = null
)