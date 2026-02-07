package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeDTO (
    @SerialName("name")
    val name: String?,
    @SerialName("position")
    val position: String?,
    @SerialName("username")
    val username: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("phoneNumber")
    val phoneNumber: String?,
    @SerialName("photoUrl")
    val photoUrl: String?,
)

