package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.domain.model.FreeTimeSlot
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.model.MeetingStatus
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.util.Result
import java.time.LocalDateTime
import java.util.UUID

/**
 * Репозиторий для работы со встречами
 */
interface MeetingRepository {

    /**
     * Создание новой встречи
     */
    suspend fun createMeeting(
        title: String,
        description: String?,
        location: String?,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        participantIds: List<UUID>
    ): Result<Meeting>

    /**
     * Получение встречи по ID
     */
    suspend fun getMeetingById(meetingId: UUID): Result<Meeting>

    /**
     * Получение списка встреч пользователя
     */
    suspend fun getUserMeetings(
        status: MeetingStatus? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<PaginatedData<Meeting>>

    /**
     * Отмена встречи
     */
    suspend fun cancelMeeting(meetingId: UUID): Result<Meeting>

    /**
     * Удаление встречи
     */
    suspend fun deleteMeeting(meetingId: UUID): Result<Unit>

    /**
     * Получение свободных временных слотов для участников
     */
    suspend fun getFreeTime(userIds: List<UUID>): Result<List<FreeTimeSlot>>
}

