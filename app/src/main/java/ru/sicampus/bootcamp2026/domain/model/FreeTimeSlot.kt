package ru.sicampus.bootcamp2026.domain.model

import java.time.LocalDateTime

/**
 * Модель свободного временного слота
 */
data class FreeTimeSlot(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

