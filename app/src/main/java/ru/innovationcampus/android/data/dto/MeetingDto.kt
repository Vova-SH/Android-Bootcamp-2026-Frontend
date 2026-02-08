package ru.innovationcampus.android.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    @SerialName("creatorName")
    val creatorName: String?,

    @SerialName("date")
    val date: String?,

    @SerialName("title")
    val title: String?,

    @SerialName("description")
    val description: String?
)