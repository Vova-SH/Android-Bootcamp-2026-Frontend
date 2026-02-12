package com.example.planify.main.features.meetings.domain.services_impl

import com.example.planify.core.utils.until
import com.example.planify.main.features.meetings.domain.entities.Meeting
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.features.meetings.domain.repositories.MeetingsRepository
import com.example.planify.main.features.meetings.domain.schemas.CreateMeetingSchema
import com.example.planify.main.features.meetings.domain.schemas.PatchMeetingSchema
import com.example.planify.main.features.meetings.domain.services.MeetingsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingsServiceImpl @Inject constructor(
    private val meetingsRepository: MeetingsRepository
) : MeetingsService {
    override suspend fun fetchMeetingContext(meetingId: Long): Result<MeetingContext> {
        return meetingsRepository.fetchMeetingContext(meetingId)
    }

    override suspend fun createMeeting(schema: CreateMeetingSchema): Result<Meeting> {
        return meetingsRepository.createMeeting(schema)
    }

    override suspend fun fetchMeeting(meetingId: Long): Result<Meeting> {
        return meetingsRepository.getMeeting(meetingId)
    }

    override suspend fun patchMeeting(meetingId: Long, patch: PatchMeetingSchema): Result<Unit> {
        return meetingsRepository.patchMeeting(meetingId, patch)
    }

    override suspend fun fetchMyDailyMeetings(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<Map<LocalDate, List<MeetingContext>>> {
        return meetingsRepository.fetchMyDailyMeetings(startDate, endDate).map { meetings ->
            val result = mutableMapOf<LocalDate, List<MeetingContext>>()

            (startDate until endDate).forEach { date ->
                result[date] = meetings[date] ?: emptyList()
            }

            result
        }
    }

    override suspend fun fetchMyDailyMeetingsShort(startDate: LocalDate, endDate: LocalDate): Result<Map<LocalDate, Int>> {
        return meetingsRepository.fetchMyDailyMeetingsShort(startDate, endDate).map { meetings ->
            val result = mutableMapOf<LocalDate, Int>()

            (startDate until endDate).forEach { date ->
                result[date] = meetings[date] ?: 0
            }

            result
        }
    }

    override suspend fun fetchUserSchedule(forDate: LocalDate): Result<Map<Int, Boolean>> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val meetings = fetchMyDailyMeetings(forDate, forDate).getOrThrow()
            val todayMeetings = meetings[forDate] ?: return@runCatching (0 until 24).associateWith { true }

            val schedule = (0 until 24).associateWith { true }.toMutableMap()

            todayMeetings.forEach { context ->  // TODO: Refactor
                (context.meeting.startsAt.hour until context.meeting.startsAt.hour + context.meeting.duration).forEach { schedule[it] = false }
            }

            schedule
        }
    }
}
