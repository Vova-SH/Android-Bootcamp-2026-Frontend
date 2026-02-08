package ru.innovationcampus.android.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagingMeetingListDto (
    @SerialName("content")
    val content: List<MeetingDto>? = null,
    @SerialName("last")
    val last: Boolean? = null
)