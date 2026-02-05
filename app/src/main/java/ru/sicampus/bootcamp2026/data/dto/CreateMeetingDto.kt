package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMeetingDto (
    @SerialName("name")
    val name: String? = null,
    @SerialName("startTime")
    val startTime: Byte? = null,
    @SerialName("endTime")
    val endTime: Byte? = null,
    @SerialName("date")
    val date: String? = null,
    @SerialName("participants")
    val participants: List<Int>? = null,
)