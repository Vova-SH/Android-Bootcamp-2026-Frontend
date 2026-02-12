package com.example.planify.main.navigation.screens.main_screen.views.home.home_view.components.entities

import java.time.LocalDate
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import kotlin.math.ceil

data class CalendarDay(
    val date: LocalDate,
    val isToday: Boolean = false,
    val isCurrentMonth: Boolean = false
)

fun getWeekDays(weekOffset: Int): List<CalendarDay> {
    val today = LocalDate.now()
    val firstDayOfWeek = today
        .plusWeeks(weekOffset.toLong())
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    return  (0..6).map { i ->
        val date = firstDayOfWeek.plusDays(i.toLong())
        CalendarDay(
            date = date,
            isToday = date == today
        )
    }
}

fun getMonthDays(
    monthOffset: Int,
    startOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    fixedWeeks: Int? = null
): List<CalendarDay> {

    val month = YearMonth.now().plusMonths(monthOffset.toLong())
    val firstDayOfMonth = month.atDay(1)

    val gridStart = firstDayOfMonth.with(
        TemporalAdjusters.previousOrSame(startOfWeek)
    )

    val offset = ((firstDayOfMonth.dayOfWeek.value - startOfWeek.value) + 7) % 7
    val neededCells = offset + month.lengthOfMonth()
    val neededWeeks = ceil(neededCells / 7.0).toInt()

    val weeks = fixedWeeks ?: neededWeeks
    val totalCells = weeks * 7

    return (0 until totalCells).map { i ->
        val date = gridStart.plusDays(i.toLong())
        CalendarDay(
            date = date,
            isCurrentMonth = (date.month == month.month
                    && date.year == month.year)
        )
    }
}