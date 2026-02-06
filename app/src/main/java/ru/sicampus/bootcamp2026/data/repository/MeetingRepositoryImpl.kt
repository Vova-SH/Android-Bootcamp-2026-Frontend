package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.mapper.toDomain
import ru.sicampus.bootcamp2026.data.remote.api.MeetingApi
import ru.sicampus.bootcamp2026.data.remote.dto.CreateMeetingRequest
import ru.sicampus.bootcamp2026.data.remote.dto.FreeTimeRequest
import ru.sicampus.bootcamp2026.domain.model.FreeTimeSlot
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.model.MeetingStatus
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

/**
 * Реализация репозитория для работы со встречами
 */
class MeetingRepositoryImpl @Inject constructor(
    private val meetingApi: MeetingApi
) : MeetingRepository {

    override suspend fun createMeeting(
        title: String,
        description: String?,
        location: String?,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        participantIds: List<UUID>
    ): Result<Meeting> {
        return try {
            val response = meetingApi.createMeeting(
                CreateMeetingRequest(
                    title = title,
                    description = description,
                    location = location,
                    startTime = startTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    endTime = endTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    participantIds = participantIds.map { it.toString() }
                )
            )
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMeetingById(meetingId: UUID): Result<Meeting> {
        return try {
            val response = meetingApi.getMeetingById(meetingId)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUserMeetings(
        status: MeetingStatus?,
        page: Int,
        size: Int
    ): Result<PaginatedData<Meeting>> {
        return try {
            val response = meetingApi.getUserMeetings(
                status = status?.name,
                page = page,
                size = size
            )
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun cancelMeeting(meetingId: UUID): Result<Meeting> {
        return try {
            val response = meetingApi.cancelMeeting(meetingId)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteMeeting(meetingId: UUID): Result<Unit> {
        return try {
            meetingApi.deleteMeeting(meetingId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getFreeTime(userIds: List<UUID>): Result<List<FreeTimeSlot>> {
        return try {
            val response = meetingApi.getFreeTime(
                FreeTimeRequest(userIds = userIds.map { it.toString() })
            )
            Result.Success(response.startEndTime.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

