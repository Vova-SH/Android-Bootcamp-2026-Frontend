package com.example.planify.main.features.meeting.domain.repositories

import com.example.planify.main.features.meeting.entities.MeetingInfo
import java.time.LocalDate

interface MeetingRepository {
    suspend fun fetchMeetingsInfo(): Map<LocalDate, List<MeetingInfo>>
}