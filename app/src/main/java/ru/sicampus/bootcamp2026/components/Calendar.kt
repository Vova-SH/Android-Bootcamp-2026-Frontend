package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.Green
import ru.sicampus.bootcamp2026.ui.theme.Inter
import ru.sicampus.bootcamp2026.ui.theme.LiteGray
import ru.sicampus.bootcamp2026.ui.theme.Red
import ru.sicampus.bootcamp2026.ui.theme.White
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class CalendarEvent(
    val date: LocalDate,
    val type: EventType
)

enum class EventType { Offline, Hybrid, Online, Another }

fun EventType.toDotColor(): Color = when (this) {
    EventType.Offline -> Blue
    EventType.Hybrid -> Green
    EventType.Online -> Red
    EventType.Another -> LiteGray
}

@Composable
fun WeekdayHeaderRow(
    modifier: Modifier = Modifier,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
) {
    val days = (0 until 7).map { offset ->
        val value = firstDayOfWeek.value + offset
        DayOfWeek.of(if (value > 7) value - 7 else value)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { dayOfWeek ->
            val label = dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("ru"))
                .lowercase()
                .replaceFirstChar { it.titlecase(Locale("ru")) }
                .take(2)

            Box(
                modifier = Modifier.width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )
            }
        }
    }
}


@Composable
fun CalendarDayItem(
    date: LocalDate,
    isSelected: Boolean,
    events: List<CalendarEvent>,
    onClick: () -> Unit,
    isInCurrentMonth: Boolean = true,
    modifier: Modifier = Modifier
) {
    val dots = events.take(3)
    val textColor = if (isInCurrentMonth) White else LiteGray.copy(alpha = 0.6f)

    Column(
        modifier = modifier
            .width(40.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        !isInCurrentMonth -> Color.Transparent
                        isSelected -> White.copy(alpha = 0.2f)
                        else -> Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (dots.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    dots.forEachIndexed { index, event ->
                        if (index < 2 || dots.size <= 2) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(event.type.toDotColor())
                            )
                        } else {
                            Text(
                                text = "…",
                                fontFamily = Inter,
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.sp,
                                lineHeight = 8.sp,
                                color = White
                            )
                            return@Row
                        }
                    }
                }
            }
        }
    }
}


private fun monthWeeks(
    yearMonth: LocalDate,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
): List<List<LocalDate>> {
    val firstOfMonth = yearMonth.withDayOfMonth(1)
    val lastOfMonth = yearMonth.withDayOfMonth(yearMonth.lengthOfMonth())

    var start = firstOfMonth
    while (start.dayOfWeek != firstDayOfWeek) {
        start = start.minusDays(1)
    }

    val weeks = mutableListOf<List<LocalDate>>()
    var current = start

    while (true) {
        val week = (0 until 7).map { current.plusDays(it.toLong()) }
        weeks += week
        current = current.plusWeeks(1)

        if (week.any { it == lastOfMonth } && current.month != yearMonth.month) break
    }

    return weeks
}

enum class CalendarMode { OneWeek, TwoWeeks, Month }

@Composable
fun AgendaCalendar(
    mode: CalendarMode,
    selectedDate: LocalDate,
    events: List<CalendarEvent>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(40.dp))
            .background(Gray)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        when (mode) {
            CalendarMode.OneWeek -> {
                val startDate = selectedDate.with(DayOfWeek.MONDAY)
                val days = (0 until 7).map { startDate.plusDays(it.toLong()) }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WeekdayHeaderRow()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        days.forEach { day ->
                            val dayEvents = events.filter { it.date == day }
                            CalendarDayItem(
                                date = day,
                                isSelected = day == selectedDate,
                                events = dayEvents,
                                onClick = { onDateSelected(day) },
                                isInCurrentMonth = true
                            )
                        }
                    }
                }
            }

            CalendarMode.TwoWeeks -> {
                val startOfCurrentWeek = LocalDate.now().with(DayOfWeek.MONDAY)
                val days = (0 until 14).map { startOfCurrentWeek.plusDays(it.toLong()) }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WeekdayHeaderRow()

                    days.chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                val dayEvents = events.filter { it.date == day }
                                CalendarDayItem(
                                    date = day,
                                    isSelected = day == selectedDate,
                                    events = dayEvents,
                                    onClick = { onDateSelected(day) },
                                    isInCurrentMonth = day.month == selectedDate.month
                                )
                            }
                        }
                    }
                }
            }

            CalendarMode.Month -> {
                val weeks = monthWeeks(selectedDate)

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WeekdayHeaderRow()

                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                val dayEvents = events.filter { it.date == day }
                                CalendarDayItem(
                                    date = day,
                                    isSelected = day == selectedDate,
                                    events = dayEvents,
                                    onClick = { onDateSelected(day) },
                                    isInCurrentMonth = day.month == selectedDate.month
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}