package com.teto.planner.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.teto.planner.domain.model.meeting.Meeting
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SharedCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    meetingsByDate: Map<LocalDate, List<Meeting>>,
    modifier: Modifier = Modifier
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) }
    val endMonth = remember { currentMonth.plusMonths(12) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
        outDateStyle = OutDateStyle.EndOfGrid
    )

    HorizontalCalendar(
        state = state,
        modifier = modifier.fillMaxWidth(),
        dayContent = { day ->
            val hasMeetings = meetingsByDate.containsKey(day.date)
            val isSelected = selectedDate == day.date

            DayCell(
                day = day,
                isSelected = isSelected,
                hasMeeting = hasMeetings,
                onClick = { clickedDay -> onDateSelected(clickedDay.date) }
            )
        },
        monthHeader = { month ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = "${
                    month.yearMonth.month.getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        Locale.forLanguageTag("ru")
                    )
                        .replaceFirstChar { it.uppercase() }
                } ${month.yearMonth.year}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            DaysOfWeekTitle(daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek })
        },
        contentPadding = PaddingValues(horizontal = 8.dp)
    )
}


@Composable
private fun DayCell(
    day: CalendarDay,
    isSelected: Boolean,
    hasMeeting: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    val isCurrentMonthDay = day.position == DayPosition.MonthDate

    val textColor = if (isCurrentMonthDay) MaterialTheme.colorScheme.onSurface else Color.LightGray.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(if (isSelected && isCurrentMonthDay) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(enabled = isCurrentMonthDay) { onClick(day) },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if (isSelected && isCurrentMonthDay) MaterialTheme.colorScheme.onPrimary else textColor,
                style = MaterialTheme.typography.bodyLarge
            )

            if (hasMeeting && isCurrentMonthDay) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

@Composable
private fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))
                    .uppercase(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
