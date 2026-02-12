package ru.sicampus.bootcamp2026.ui.screen.meetings.calendar.week

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.sicampus.bootcamp2026.ui.screen.meetings.calendar.DayItem
import java.time.LocalDate

@Composable
fun WeekCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember {
        LocalDate.now()
    }
    val startOfCurrentWeek = remember {
        getStartOfWeek(LocalDate.now())
    }
    val initialPage = remember(selectedDate) {
        val weeksBetween =
            java.time.temporal.ChronoUnit.WEEKS.between(
                startOfCurrentWeek,
                getStartOfWeek(selectedDate)
            )
        5000 + weeksBetween.toInt()
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { 10000 }
    )
    LaunchedEffect (pagerState.currentPage) {
        val weekStart =
            startOfCurrentWeek.plusDays((pagerState.currentPage - 5000) * 7L)
        val newSelectedDate =
            weekStart.plusDays((selectedDate.dayOfWeek.value - 1).toLong())
        if (newSelectedDate != selectedDate) {
            onDateSelected(newSelectedDate)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        val weekStart = startOfCurrentWeek.plusDays(
            (page - 5000) * 7L
        )
        val days = generateWeek(weekStart)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { date ->
                DayItem(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == today,
                    onClick = {
                        onDateSelected(date)
                    }
                )
            }
        }
    }
}