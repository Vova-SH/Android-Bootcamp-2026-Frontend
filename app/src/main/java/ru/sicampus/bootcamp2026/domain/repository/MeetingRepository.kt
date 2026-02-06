package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.domain.model.Meeting

interface MeetingRepository {
    suspend fun getMeetings(page: Int, size: Int): Result<List<Meeting>>
    suspend fun createMeeting(title: String, description: String, place: String, start: String, duration: Int): Result<Unit>
    suspend fun deleteMeeting(id: Long): Result<Unit>
}