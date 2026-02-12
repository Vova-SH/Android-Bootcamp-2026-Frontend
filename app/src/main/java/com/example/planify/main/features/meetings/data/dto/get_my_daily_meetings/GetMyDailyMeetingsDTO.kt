package com.example.planify.main.features.meetings.data.dto.get_my_daily_meetings

import com.example.planify.main.features.meetings.data.dto.MeetingContextDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyDailyMeetingsDTO(
    @SerialName("meetings")
    val meetings: Map<String, List<MeetingContextDTO>>
)
