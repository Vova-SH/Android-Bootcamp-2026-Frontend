package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.data.dto.MemberDto

interface MeetingRepository {
    suspend fun getMeetings(page: Int, size: Int): Result<List<Meeting>>
    suspend fun getSchedule(): Result<List<Meeting>>
    suspend fun getMeetingById(id: Long): Result<Meeting>
    suspend fun getMeetingMembers(id: Long): Result<List<MemberDto>>
    suspend fun createMeeting(title: String, description: String, place: String, start: String, duration: Int): Result<Unit>
    suspend fun updateMeeting(id: Long, title: String, description: String, place: String, start: String, duration: Int): Result<Unit>
    suspend fun deleteMeeting(id: Long): Result<Unit>
}