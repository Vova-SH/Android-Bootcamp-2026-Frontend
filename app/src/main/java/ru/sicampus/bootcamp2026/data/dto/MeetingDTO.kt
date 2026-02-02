package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("startTime")
    val startTime: Byte?,
    @SerialName("endTime")
    val endTime: Byte?,
    @SerialName("name")
    val name: String?,
    @SerialName("location")
    val location: String?,
    @SerialName("date")
    val date: String?,
    @SerialName("participants")
    val participants: List<Int>?,
)
