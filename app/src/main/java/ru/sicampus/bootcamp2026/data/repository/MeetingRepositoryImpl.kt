package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.MeetingInputDto
import ru.sicampus.bootcamp2026.data.dto.MemberDto
import ru.sicampus.bootcamp2026.data.source.MeetingService
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class MeetingRepositoryImpl : MeetingRepository {
    private val service = MeetingService()

    override suspend fun getMeetings(page: Int, size: Int): Result<List<Meeting>> {
        return try {
            val pageDto = service.getMeetingsPaginated(page, size)
            Result.success(pageDto.content.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSchedule(): Result<List<Meeting>> {
        return try {
            val meetings = service.getSchedule()
            Result.success(meetings.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMeetingById(id: Long): Result<Meeting> {
        return try {
            val dto = service.getMeetingById(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMeetingMembers(id: Long): Result<List<MemberDto>> {
        return try {
            val members = service.getMeetingMembers(id)
            Result.success(members)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createMeeting(
        title: String,
        description: String,
        place: String,
        start: String,
        duration: Int
    ): Result<Unit> {
        return try {
            val input = MeetingInputDto(
                start = start,
                duration = duration,
                place = place,
                theme = title,
                description = description
            )
            service.createMeeting(input)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMeeting(
        id: Long,
        title: String,
        description: String,
        place: String,
        start: String,
        duration: Int
    ): Result<Unit> {
        return try {
            val input = MeetingInputDto(
                start = start,
                duration = duration,
                place = place,
                theme = title,
                description = description
            )
            service.updateMeeting(id, input)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMeeting(id: Long): Result<Unit> {
        return try {
            service.deleteMeeting(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun MeetingDto.toDomain(): Meeting {
        val creatorName = creator?.let { "${it.firstName} ${it.secondName}" } ?: "Неизвестно"
        val creatorId = creator?.id ?: -1L

        val effectiveTitle = theme?.takeIf { it.isNotBlank() } ?: "Без темы"

        return Meeting(
            id = id,
            title = effectiveTitle,
            description = description,
            location = place,
            startTime = start,
            durationMinutes = duration,
            creatorName = creatorName,
            creatorId = creatorId
        )
    }
}