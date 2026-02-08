package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("photoUrl")
    val photoUrl: String? = null,
    @SerialName("departmentName")
    val departmentName: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null
)
