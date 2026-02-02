package ru.sicampus.bootcamp2026.domain.model

data class Meeting(
    val id: Long,
    val title: String,
    val location: String,
    val startTime: String,
    val durationMinutes: Int
)