package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("photoUrl")
    val photoUrl: String?,
    @SerialName("username")
    val username: String?,
    @SerialName("surname")
    val surname: String?,
    @SerialName("patronymic")
    val patronymic: String?,
    @SerialName("messengerLink")
    val messengerLink: String?,
    @SerialName("phoneNumber")
    val phoneNumber: String?,
    @SerialName("departmentName")
    val departmentName: String?
)
