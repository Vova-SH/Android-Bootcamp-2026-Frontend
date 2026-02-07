package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.data.dto.user.UserDTO


@Serializable
data class PagingUserListDTO(
    @SerialName("content")
    val content: List<UserDTO>? = null
)