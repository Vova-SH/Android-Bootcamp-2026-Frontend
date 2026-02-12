package com.example.planify.main.features.profiles.data.dto.get_my_profile

import com.example.planify.main.common.dto.ProfileDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyProfileResponseDTO (
    @SerialName("profile")
    val profile: ProfileDTO
)