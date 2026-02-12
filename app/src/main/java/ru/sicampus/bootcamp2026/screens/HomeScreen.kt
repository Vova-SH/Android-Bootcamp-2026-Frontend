package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.components.*
import ru.sicampus.bootcamp2026.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.White
import ru.sicampus.bootcamp2026.data.repo.BootcampRepository
import ru.sicampus.bootcamp2026.data.util.toMeetingUi
import ru.sicampus.bootcamp2026.data.util.toUiMessage
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


data class DatedMeeting(
    val date: LocalDate,
    val meeting: MeetingUi
)



private fun groupedMeetings(
    meetings: List<DatedMeeting>
): List<Pair<LocalDate, List<DatedMeeting>>> =
    meetings.groupBy { it.date }.toSortedMap().toList()


@Composable
fun HomeScreen(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val selectedDate = today

    val repo = remember { BootcampRepository() }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var allMeetings by remember { mutableStateOf<List<DatedMeeting>>(emptyList()) }

    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        try {
            val dtos = repo.getWeekSchedule()
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

    val grouped = remember(allMeetings) { groupedMeetings(allMeetings) }

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

            if (isLoading) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Загружаем расписание...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White
                        )
                    }
                }
            }

            if (!error.isNullOrBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Не удалось загрузить расписание: ${error}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White
                        )
                    }
                }
            }

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

                    Text(
                        text = "$monthLabel, $dayNumber",
                        style = MaterialTheme.typography.titleMedium,
                        color = White
                    )

                    Spacer(Modifier.height(16.dp))

                    AgendaCalendar(
                        mode = CalendarMode.OneWeek,
                        selectedDate = today,
                        events = allMeetings.map {
                            CalendarEvent(it.date, modeToEventType(it.meeting.mode))
                        },
                        onDateSelected = { },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            items(grouped) { (date, dayMeetings) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val header = when {
                        date == today -> "Сегодня"
                        date == today.plusDays(1) -> "Завтра"
                        else -> {
                            val day = date.dayOfMonth
                            val month = String.format("%02d", date.monthValue)
                            "$day.$month"
                        }
                    }


                    Text(
                        text = header,
                        style = MaterialTheme.typography.titleMedium,
                        color = White
                    )

                    Spacer(Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dayMeetings.forEach { dated ->
                            MeetingCard(meeting = dated.meeting)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(96.dp)) }
        }

        MainTopBar(
            title = "Активные встречи",
            modifier = Modifier
                .align(Alignment.TopCenter)
        )

        MainBottomBar(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}



@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            currentTab = MainTab.Home,
            onTabSelected = {}
        )
    }
}
