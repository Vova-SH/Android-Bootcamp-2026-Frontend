package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    val id: Long,
    val start: String,
    val duration: Int,
    val place: String,
    @SerialName("theme")
    val theme: String? = null,
    val description: String? = null,
    val creator: UserDto? = null
)