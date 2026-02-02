package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.domain.model.Meeting

interface MeetingRepository {
    suspend fun getMeetings(): List<Meeting>
}