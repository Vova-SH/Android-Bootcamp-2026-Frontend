package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    @SerialName("id") val id: Long,
    @SerialName("start") val start: String,
    @SerialName("duration") val duration: Int,
    @SerialName("place") val place: String,
    @SerialName("theme") val theme: String,
    @SerialName("description") val description: String?,
    @SerialName("creator") val creatorId: Long
)