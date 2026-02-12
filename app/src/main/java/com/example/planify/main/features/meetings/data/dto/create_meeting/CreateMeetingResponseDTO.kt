package com.example.planify.main.features.meetings.data.dto.create_meeting

import com.example.planify.main.features.meetings.data.dto.MeetingDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMeetingResponseDTO(
    @SerialName("meeting")
    val meeting: MeetingDTO
)
