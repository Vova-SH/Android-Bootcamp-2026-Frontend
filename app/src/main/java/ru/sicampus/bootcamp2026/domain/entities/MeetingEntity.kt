package ru.sicampus.bootcamp2026.domain.entities

import java.time.LocalDate
import java.time.LocalDateTime

data class MeetingEntity(
    val id: Long,
    val title: String,
    val organizerId: Long,
    val date: LocalDate,
    val startHour: Int,
    val createdAt: LocalDateTime?
)
