package com.example.create_meet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMeetingDto(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("location")
    val location: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("durationHours")
    val durationHours: Int,
    @SerialName("invitees")
    val invitees: List<String>
)