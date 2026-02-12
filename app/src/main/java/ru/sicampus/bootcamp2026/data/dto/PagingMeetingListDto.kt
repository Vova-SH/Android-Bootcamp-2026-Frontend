package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingMeetingListDto (
    @SerialName("content")
    val content: List<MeetingDTO>? = null,
    @SerialName("last")
    val last: Boolean? = null
)