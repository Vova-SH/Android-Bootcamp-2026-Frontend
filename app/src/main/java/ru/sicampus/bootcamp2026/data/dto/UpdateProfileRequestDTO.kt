package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.entities.ProfileUpdateEntity

@Serializable
data class UpdateProfileRequestDTO(
    @SerialName("name")
    val name: String,
    @SerialName("position")
    val position: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
)
fun ProfileUpdateEntity.toDTO(): UpdateProfileRequestDTO {
    return UpdateProfileRequestDTO(
        name = this.name,
        position = this.position,
        email = this.email,
        phoneNumber = this.phoneNumber,
    )
}