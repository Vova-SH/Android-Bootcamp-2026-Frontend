package ru.sicampus.bootcamp2026.domain.models.meet

import java.time.LocalDate
import java.time.LocalDateTime

data class MeetTimeSlot(
    val date: LocalDate,
    val startHour: Int,
    val durationHours: Int
)
