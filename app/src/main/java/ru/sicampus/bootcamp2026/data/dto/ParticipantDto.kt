package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    @SerialName("id")
    val id: Int,

    @SerialName("fullName")
    val fullName: String,

    @SerialName("status")
    val status: String
)