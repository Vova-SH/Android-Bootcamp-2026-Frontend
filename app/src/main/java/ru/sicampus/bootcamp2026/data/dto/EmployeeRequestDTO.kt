package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.ui.screen.auth.register.RegisterFields

@Serializable
data class EmployeeRequestDTO(
    @SerialName("name")
    val name: String,
    @SerialName("position")
    val position: String,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("password")
    val password: String,
)
fun RegisterFields.toDTO(): EmployeeRequestDTO {
    return EmployeeRequestDTO(
        name = this.name,
        position = this.position,
        username = this.username,
        email = this.email,
        phoneNumber = this.phoneNumber,
        password = this.password
    )
}