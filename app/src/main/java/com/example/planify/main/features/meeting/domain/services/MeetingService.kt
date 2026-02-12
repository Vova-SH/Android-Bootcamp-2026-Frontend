package com.example.planify.main.features.meeting.domain.services

import com.example.planify.main.features.meeting.entities.MeetingInfo
import java.time.LocalDate

interface MeetingService {
    suspend fun fetchMeetingsInfo(): Map<LocalDate, List<MeetingInfo>>
}