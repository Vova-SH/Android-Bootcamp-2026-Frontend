package com.example.planify.main.features.profiles.data.dto.search

import com.example.planify.main.common.dto.PageDTO
import com.example.planify.main.common.dto.ProfileDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchProfileResponseDTO (
    @SerialName("result")
    val result: PageDTO
)