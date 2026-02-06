package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.MeetingInputDto
import ru.sicampus.bootcamp2026.data.source.MeetingService
import ru.sicampus.bootcamp2026.data.source.SessionManager
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

    override suspend fun createMeeting(
        title: String,
        description: String,
        place: String,
        start: String,
        duration: Int
    ): Result<Unit> {
        return try {
            val userId = SessionManager.currentUserId ?: throw IllegalStateException("User not logged in")
            val input = MeetingInputDto(
                start = start,
                duration = duration,
                place = place,
                theme = title,
                description = description
            )
            service.createMeeting(userId, input)
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
        val creatorName = creator?.let { "${it.firstName} ${it.secondName}" } ?: "Unknown"
        return Meeting(
            id = id,
            title = theme ?: "Без темы",
            description = description,
            location = place ?: "Место не указано",
            startTime = start.replace("T", " "),
            durationMinutes = duration,
            creatorName = creatorName
        )
    }
}