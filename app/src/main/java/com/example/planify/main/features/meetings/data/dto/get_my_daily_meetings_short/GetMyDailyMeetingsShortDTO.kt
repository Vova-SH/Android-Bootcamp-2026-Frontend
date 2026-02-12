package com.example.planify.main.features.meetings.data.dto.get_my_daily_meetings_short

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyDailyMeetingsShortDTO(
    @SerialName("meetings")
    val meetings: Map<String, Int>
)
