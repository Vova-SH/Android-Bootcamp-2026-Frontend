package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.models.User
import java.time.Instant


@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("email")
    val email: String?,
    @SerialName("firstName")
    val firstName: String?,
    @SerialName("lastName")
    val lastName: String?,
    @SerialName("photoUrl")
    val photoUrl: String? = null
)