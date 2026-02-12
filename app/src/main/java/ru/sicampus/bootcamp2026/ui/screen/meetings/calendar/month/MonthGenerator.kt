package ru.sicampus.bootcamp2026.ui.screen.meetings.calendar.month

import java.time.DayOfWeek
import java.time.LocalDate

fun generateMonth(
    selectedDate: LocalDate
): List<LocalDate?> {
    val firstDayOfMonth =
        selectedDate.withDayOfMonth(1)
    val lastDayOfMonth =
        selectedDate.withDayOfMonth(
            selectedDate.lengthOfMonth()
        )
    val firstWeekDay =
        firstDayOfMonth.dayOfWeek
    val offset =
        if (firstWeekDay == DayOfWeek.SUNDAY)
            6
        else
            firstWeekDay.value - 1
    val totalDays =
        offset + lastDayOfMonth.dayOfMonth
    val totalCells =
        if (totalDays <= 35) 35 else 42
    return List(totalCells) { index ->
        val dayNumber =
            index - offset + 1
        if (dayNumber in 1..lastDayOfMonth.dayOfMonth)
            LocalDate.of(
                selectedDate.year,
                selectedDate.month,
                dayNumber
            )
        else
            null
    }
}
