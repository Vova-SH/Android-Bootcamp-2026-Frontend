package ru.sicampus.bootcamp2026.data.repository

import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.source.Network
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.domain.repository.MeetingRepository

class MeetingRepositoryImpl : MeetingRepository {

    private val client = Network.client

    override suspend fun getMeetings(): List<Meeting> {
        return try {
            val dtos: List<MeetingDto> = client.get("/api/meetings").body()
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun MeetingDto.toDomain(): Meeting {
        return Meeting(
            id = id,
            title = theme,
            location = place,
            startTime = start,
            durationMinutes = duration
        )
    }
}