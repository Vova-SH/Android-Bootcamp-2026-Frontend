package com.example.planify.main.features.meetings.domain.repositories

import com.example.planify.main.features.meetings.domain.entities.Meeting
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.features.meetings.domain.schemas.CreateMeetingSchema
import com.example.planify.main.features.meetings.domain.schemas.PatchMeetingSchema
import java.time.LocalDate

interface MeetingsRepository {
    suspend fun fetchMeetingContext(meetingId: Long): Result<MeetingContext>
    suspend fun createMeeting(schema: CreateMeetingSchema): Result<Meeting>
    suspend fun getMeeting(meetingId: Long): Result<Meeting>
    suspend fun patchMeeting(meetingId: Long, patch: PatchMeetingSchema): Result<Unit>

    suspend fun fetchMyDailyMeetings(startDate: LocalDate, endDate: LocalDate): Result<Map<LocalDate, List<MeetingContext>>>
    suspend fun fetchMyDailyMeetingsShort(startDate: LocalDate, endDate: LocalDate): Result<Map<LocalDate, Int>>
}
