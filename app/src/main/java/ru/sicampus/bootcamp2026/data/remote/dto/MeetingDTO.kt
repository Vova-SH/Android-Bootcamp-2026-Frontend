package ru.sicampus.bootcamp2026.data.remote.dto

import java.time.Instant
import java.util.UUID

// DTO для получения списка встреч с сервера
data class MeetingDTO(
    val id: UUID,
    val title: String,
    val description: String?,
    val date: Instant,
    val startTime: String,
    val endTime: String,
    val location: String?,
    val participants: List<String> // список ID или имен участников
)
