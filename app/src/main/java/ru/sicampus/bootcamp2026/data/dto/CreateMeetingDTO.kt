package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMeetingDTO (
    @SerialName("name")
    val name: String?,
    @SerialName("startTime")
    val startTime: Byte?,
    @SerialName("endTime")
    val endTime: Byte?,
    @SerialName("date")
    val date: String?,
    @SerialName("participants")
    val participants: List<Int>?,
)