package com.example.planify.core.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

fun LocalDate.weekBounds(
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
): Pair<LocalDateTime, LocalDateTime> {
    val weekStart = this.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay()
    val weekEnd = weekStart.plusDays(6).toLocalDate().atTime(LocalTime.MAX)
    return weekStart to weekEnd
}

infix fun LocalDate.until(end: LocalDate): Sequence<LocalDate> =
    generateSequence(this) { current ->
        current.plusDays(1).takeIf { it <= end }
    }
