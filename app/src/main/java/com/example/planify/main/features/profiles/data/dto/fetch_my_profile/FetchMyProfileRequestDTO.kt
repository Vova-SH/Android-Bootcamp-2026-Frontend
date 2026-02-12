package com.example.planify.main.features.profiles.data.dto.fetch_my_profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchMyProfileRequestDTO (
    @SerialName("firstName")
    val firstName: String?,
    @SerialName("lastName")
    val lastName: String?,
    @SerialName("position")
    val position: String?,
    @SerialName("department")
    val department: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?
)