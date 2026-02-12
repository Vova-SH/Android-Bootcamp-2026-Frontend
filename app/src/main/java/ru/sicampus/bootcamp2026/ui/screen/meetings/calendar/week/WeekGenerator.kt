package ru.sicampus.bootcamp2026.ui.screen.meetings.calendar.week

import java.time.DayOfWeek
import java.time.LocalDate

fun getStartOfWeek(date: LocalDate): LocalDate {
    var current = date

    while (current.dayOfWeek != DayOfWeek.MONDAY) {
        current = current.minusDays(1)
    }

    return current
}

fun generateWeek(startOfWeek: LocalDate): List<LocalDate> {
    return List(7) { index ->
        startOfWeek.plusDays(index.toLong())
    }
}