package com.example.meet.domain.repository

import com.example.meet.domain.entity.Meeting

interface MeetingRepository {
    suspend fun getMeetings(userId: Int? = null): Result<List<Meeting>>
    suspend fun getMeetingById(id: Int): Result<Meeting>
    suspend fun createMeeting(
        title: String,
        description: String?,
        startTime: String,
        endTime: String,
        participantIds: List<Int>
    ): Result<Meeting>
}