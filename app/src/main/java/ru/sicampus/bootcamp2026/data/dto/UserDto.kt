package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    @SerialName("name")
    val name: String?,
    @SerialName("lastName")
    val lastName: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("login")
    val login: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("phoneNumber")
    val phoneNumber: String?,
    @SerialName("password")
    val password: String?,
    @SerialName("department")
    val department: String?,
    @SerialName("position")
    val position: String?,
    @SerialName("photoUrl")
    val photoUrl: String?,
)