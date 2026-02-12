package com.example.planify.main.features.meetings.data.dto.get_meeting_context

import com.example.planify.main.features.meetings.data.dto.MeetingContextDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMeetingContextResponseDTO (
    @SerialName("meetingContext")
    val meetingContext: MeetingContextDTO
)