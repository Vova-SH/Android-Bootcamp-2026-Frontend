package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeetingInputDto(
    val start: String,
    val duration: Int,
    val place: String,
    val theme: String,
    val description: String?
)