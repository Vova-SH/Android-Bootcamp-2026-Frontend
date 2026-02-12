package com.example.planify.main.common.dto

import com.example.planify.main.features.profiles.domain.entities.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    @SerialName("userId")
    val userId: Long,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("position")
    val position: String?,
    @SerialName("department")
    val department: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String
) {
    fun toEntity(): Profile = Profile(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        position = position ?: "",
        department = department ?: "",
        profileImageUrl = profileImageUrl
    )
}
