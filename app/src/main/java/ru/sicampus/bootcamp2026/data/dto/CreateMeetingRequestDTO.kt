package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateMeetingRequestDTO (
    val name: String,
    val description: String,
    val startTime: String,
    val endTime: String,
)
