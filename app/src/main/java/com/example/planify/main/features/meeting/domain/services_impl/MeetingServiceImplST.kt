package com.example.planify.main.features.meeting.domain.services_impl

import com.example.planify.core.SingletonHolder
import com.example.planify.main.features.meeting.domain.repositories.MeetingRepository
import com.example.planify.main.features.meeting.domain.services.MeetingService
import com.example.planify.main.features.meeting.entities.MeetingInfo
import java.time.LocalDate

class MeetingServiceImplST private constructor(
    val meetingRepository: MeetingRepository
): MeetingService {
    override suspend fun fetchMeetingsInfo(): Map<LocalDate, List<MeetingInfo>> {
        return meetingRepository.fetchMeetingsInfo()
    }

    companion object : SingletonHolder<MeetingServiceImplST, MeetingRepository>(::MeetingServiceImplST)
}