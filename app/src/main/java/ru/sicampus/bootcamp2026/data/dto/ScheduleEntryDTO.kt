package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleEntryDTO (
    @SerialName("topic")
    val topic: String,
    @SerialName("dateTime")
    val dateTime: String
)