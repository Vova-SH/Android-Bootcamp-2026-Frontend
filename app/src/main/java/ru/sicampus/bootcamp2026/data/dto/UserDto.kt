package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    @SerialName("id")
    val id : Int?,
    @SerialName("email")
    val email: String?,
    @SerialName("fullName")
    val fullName: String?,
)