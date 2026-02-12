package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.components.AgendaCalendar
import ru.sicampus.bootcamp2026.components.CalendarEvent
import ru.sicampus.bootcamp2026.components.CalendarMode
import ru.sicampus.bootcamp2026.components.MainBottomBar
import ru.sicampus.bootcamp2026.components.MainTab
import ru.sicampus.bootcamp2026.components.MainTopBar
import ru.sicampus.bootcamp2026.components.MeetingCard
import ru.sicampus.bootcamp2026.data.repo.BootcampRepository
import ru.sicampus.bootcamp2026.data.util.toMeetingUi
import ru.sicampus.bootcamp2026.data.util.toUiMessage
import ru.sicampus.bootcamp2026.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.White
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()

    val repo = remember { BootcampRepository() }
    var selectedDate by remember { mutableStateOf(today) }
    var calendarMode by remember { mutableStateOf(CalendarMode.OneWeek) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var allMeetings by remember { mutableStateOf<List<DatedMeeting>>(emptyList()) }

    LaunchedEffect(calendarMode) {

        isLoading = true
        error = null
        selectedDate = today
        try {
            val dtos = when (calendarMode) {
                CalendarMode.OneWeek -> repo.getWeekSchedule()
                CalendarMode.TwoWeeks -> repo.getTwoWeeksSchedule()
                CalendarMode.Month -> repo.getMonthSchedule()
            }
            allMeetings = dtos.map { dto ->
                val (date, ui) = dto.toMeetingUi(today)
                DatedMeeting(date = date, meeting = ui)
            }
        } catch (e: Exception) {
            error = e.toUiMessage()
            allMeetings = emptyList()
        } finally {
            isLoading = false
        }
    }

    val meetingsForSelectedDay = remember(selectedDate, allMeetings) {
        allMeetings.filter { it.date == selectedDate }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(136.dp)) }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val monthLabel = selectedDate.month
                        .getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
                        .replaceFirstChar { it.titlecase(Locale("ru")) }
                    val dayNumber = selectedDate.dayOfMonth

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalendarMode.values().forEach { mode ->
                            val selected = mode == calendarMode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (selected) White.copy(alpha = 0.12f) else DarkBlue
                                    )
                                    .clickable { calendarMode = mode },
                                contentAlignment = Alignment.Center
                            ) {
                                val label = when (mode) {
                                    CalendarMode.OneWeek -> "Неделя"
                                    CalendarMode.TwoWeeks -> "2 недели"
                                    CalendarMode.Month -> "Месяц"
                                }
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = White
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    if (isLoading) {
                        Text(
                            text = "Загружаем календарь...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    if (!error.isNullOrBlank()) {
                        Text(
                            text = "Не удалось загрузить данные: $error",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    AgendaCalendar(
                        mode = calendarMode,
                        selectedDate = selectedDate,
                        events = allMeetings.map {
                            CalendarEvent(it.date, modeToEventType(it.meeting.mode))
                        },
                        onDateSelected = { day -> selectedDate = day },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "$monthLabel, $dayNumber",
                        style = MaterialTheme.typography.titleMedium,
                        color = White
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .heightIn(min = 200.dp),
                    contentAlignment = if (meetingsForSelectedDay.isEmpty()) {
                        Alignment.Center
                    } else {
                        Alignment.TopStart
                    }
                ) {
                    if (meetingsForSelectedDay.isEmpty()) {
                        Text(
                            text = "На сегодня встреч не запланированно",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            meetingsForSelectedDay.forEach { dated ->
                                MeetingCard(meeting = dated.meeting)
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(96.dp)) }
        }

        MainTopBar(
            title = "Календарь мероприятий",
            modifier = Modifier.align(Alignment.TopCenter)
        )

        MainBottomBar(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun CalendarScreenPreview() {
    AppTheme {
        CalendarScreen(
            currentTab = MainTab.Calendar,
            onTabSelected = {}
        )
    }
}
