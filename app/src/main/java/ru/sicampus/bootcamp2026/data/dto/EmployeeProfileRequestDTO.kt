package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.entities.EmployeeEntity

@Serializable
data class EmployeeProfileRequestDTO(
    @SerialName("name") val name: String,
    @SerialName("position") val position: String,
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("phoneNumber") val phoneNumber: String,
)
fun EmployeeEntity.toDTO(): EmployeeProfileRequestDTO {
    return EmployeeProfileRequestDTO(
        name = this.name,
        position = this.position,
        username = this.username,
        email = this.email,
        phoneNumber = this.phoneNumber,
    )
}