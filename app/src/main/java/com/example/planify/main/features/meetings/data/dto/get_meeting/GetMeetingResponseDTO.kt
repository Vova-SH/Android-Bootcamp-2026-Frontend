package com.example.planify.main.features.meetings.data.dto.get_meeting

import com.example.planify.main.features.meetings.data.dto.MeetingDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMeetingResponseDTO(
    @SerialName("meeting")
    val meeting: MeetingDTO
)
