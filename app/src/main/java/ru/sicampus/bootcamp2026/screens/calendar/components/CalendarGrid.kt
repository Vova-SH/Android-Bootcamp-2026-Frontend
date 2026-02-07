package ru.sicampus.bootcamp2026.screens.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarGrid(
    modifier: Modifier = Modifier
) {
    val calendarData = listOf(
        listOf(28, 29, 30, 31, 1, 2, 3),
        listOf(4, 5, 6, 7, 8, 9, 10),
        listOf(11, 12, 13, 14, 15, 16, 17),
        listOf(18, 19, 20, 21, 22, 23, 24),
        listOf(25, 26, 27, 28, 29, 30, 31),
        listOf(1, 2, 3, 4, 5, 6, 7)
    )

    val meetingDays = listOf(19, 21, 28)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DayOfWeekHeader()

            calendarData.forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    week.forEach { day ->
                        CalendarDayCell(
                            day = day,
                            isCurrentMonth = day in 1..31,
                            isToday = day == 15,
                            hasMeeting = day in meetingDays,
                            onClick = {  },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MeetingTimeIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "19:00",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CalendarGridPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
        ) {
            CalendarGrid()
        }
    }
}