package com.example.meet.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet.R
import com.example.meet.data.dto.MeetingDto
import com.example.meet.data.source.DataLocator
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

enum class ScheduleTab(val title: String) {
    DAY("День"),
    WEEK("Неделя"),
    MONTH("Месяц")
}

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val meetings: List<MeetingDto>) : ScheduleUiState()
    data class Error(val message: String) : ScheduleUiState()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun ScheduleScreen(navController: NavHostController) {
    val ds = remember { DataLocator.userInfoDataSource }
    val scope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf<ScheduleUiState>(ScheduleUiState.Loading) }
    var selectedTab by remember { mutableStateOf(ScheduleTab.DAY) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentWeekStart by remember { mutableStateOf(LocalDate.now().with(DayOfWeek.MONDAY)) }
    var currentMonthDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val meetings = ds.loadMeetingsForCurrentUser()
                uiState = ScheduleUiState.Success(meetings)
            } catch (e: Exception) {
                uiState = ScheduleUiState.Error("Не удалось загрузить расписание: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Расписание",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("main_meet") {
                                    popUpTo("main_meet") { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Назад",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                        height = 3.dp,
                        color = Color.White
                    )
                }
            ) {
                ScheduleTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab.ordinal == index,
                        onClick = { selectedTab = tab },
                        text = {
                            Text(
                                tab.title,
                                fontWeight = if (selectedTab.ordinal == index) FontWeight.SemiBold else FontWeight.Normal,
                                color = Color.White
                            )
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when (val state = uiState) {
                    ScheduleUiState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )

                    is ScheduleUiState.Error -> Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    is ScheduleUiState.Success -> {
                        when (selectedTab) {
                            ScheduleTab.DAY -> DaySchedule(
                                meetings = state.meetings,
                                date = selectedDate,
                                onDateChange = { selectedDate = it }
                            )
                            ScheduleTab.WEEK -> WeekSchedule(
                                meetings = state.meetings,
                                weekStart = currentWeekStart,
                                onWeekChange = { currentWeekStart = it },
                                onSelectDay = {
                                    selectedDate = it
                                    selectedTab = ScheduleTab.DAY
                                }
                            )
                            ScheduleTab.MONTH -> MonthSchedule(
                                meetings = state.meetings,
                                monthDate = currentMonthDate,
                                onMonthChange = { currentMonthDate = it },
                                onSelectDay = {
                                    selectedDate = it
                                    selectedTab = ScheduleTab.DAY
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateNavigationHeader(
    dateText: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPrevious,
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Предыдущий",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = dateText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        IconButton(
            onClick = onNext,
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = "Следующий",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun DaySchedule(
    meetings: List<MeetingDto>,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val parsedMeetings = remember(meetings) {
        meetings.mapNotNull { meeting ->
            parseMeetingDateTime(meeting)?.let { meeting to it }
        }
    }
    val dayMeetings = parsedMeetings.filter { (_, range) ->
        range.first.toLocalDate() == date
    }

    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val dateText = date.format(formatter)

    Column(modifier = Modifier.fillMaxSize()) {
        DateNavigationHeader(
            dateText = dateText,
            onPrevious = { onDateChange(date.minusDays(1)) },
            onNext = { onDateChange(date.plusDays(1)) }
        )

        if (dayMeetings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "На этот день встреч не запланировано",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dayMeetings) { (meeting, dateTimeRange) ->
                    val formattedTime = formatMeetingTime(dateTimeRange.first, dateTimeRange.second)
                    EnhancedMeetingCard(
                        meeting = meeting,
                        formattedTime = formattedTime
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekSchedule(
    meetings: List<MeetingDto>,
    weekStart: LocalDate,
    onWeekChange: (LocalDate) -> Unit,
    onSelectDay: (LocalDate) -> Unit
) {
    val daysOfWeek = (0..6).map { weekStart.plusDays(it.toLong()) }
    val parsedMeetings = remember(meetings) {
        meetings.mapNotNull { meeting ->
            parseMeetingDateTime(meeting)?.let { meeting to it }
        }
    }

    val dateText = "${daysOfWeek.first().dayOfMonth} - ${daysOfWeek.last().dayOfMonth} " +
            daysOfWeek.first().month.name.lowercase().replaceFirstChar { it.uppercase() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DateNavigationHeader(
            dateText = dateText,
            onPrevious = { onWeekChange(weekStart.minusWeeks(1)) },
            onNext = { onWeekChange(weekStart.plusWeeks(1)) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(daysOfWeek) { day ->
                val dayMeetings = parsedMeetings.filter { (_, range) ->
                    range.first.toLocalDate() == day
                }
                DaySection(
                    day = day,
                    meetings = dayMeetings.map { it.first },
                    dateTimeRanges = dayMeetings.map { it.second },
                    onSelectDay = onSelectDay
                )
            }
        }
    }
}

@Composable
private fun DaySection(
    day: LocalDate,
    meetings: List<MeetingDto>,
    dateTimeRanges: List<Pair<LocalDateTime, LocalDateTime>>,
    onSelectDay: (LocalDate) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM")
    val dayText = day.format(formatter)
        .replaceFirstChar { it.uppercase() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(day) {
                detectTapGestures {
                    onSelectDay(day)
                }
            },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(
            alpha = if (isPressed) 0.08f else 0.04f
        ),
        tonalElevation = if (isPressed) 2.dp else 0.dp,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dayText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (meetings.isNotEmpty()) {
                    Text(
                        text = "${meetings.size} встреч",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }

            if (meetings.isEmpty()) {
                Text(
                    text = "Встреч нет",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    meetings.forEachIndexed { index, meeting ->
                        val formattedTime = formatMeetingTime(
                            dateTimeRanges[index].first,
                            dateTimeRanges[index].second
                        )
                        EnhancedMeetingCard(
                            meeting = meeting,
                            formattedTime = formattedTime
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthSchedule(
    meetings: List<MeetingDto>,
    monthDate: LocalDate,
    onMonthChange: (LocalDate) -> Unit,
    onSelectDay: (LocalDate) -> Unit
) {
    val yearMonth = YearMonth.of(monthDate.year, monthDate.month)
    val firstOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeekIndex = firstOfMonth.dayOfWeek.value % 7
    val parsedMeetings = remember(meetings) {
        meetings.mapNotNull { meeting ->
            parseMeetingDateTime(meeting)?.let { meeting to it }
        }
    }
    val daysWithMeetings = parsedMeetings
        .map { it.second.first.toLocalDate() }
        .filter { it.month == monthDate.month && it.year == monthDate.year }
        .toSet()

    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val monthText = monthDate.format(formatter)
        .replaceFirstChar { it.uppercase() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DateNavigationHeader(
            dateText = monthText,
            onPrevious = { onMonthChange(monthDate.minusMonths(1)) },
            onNext = { onMonthChange(monthDate.plusMonths(1)) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f).padding(4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        val totalCells = firstDayOfWeekIndex + daysInMonth
        val rows = (totalCells + 6) / 7
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(rows) { rowIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(7) { columnIndex ->
                        val cellIndex = rowIndex * 7 + columnIndex
                        val dayNumber = cellIndex - firstDayOfWeekIndex + 1
                        if (dayNumber in 1..daysInMonth) {
                            val date = yearMonth.atDay(dayNumber)
                            val hasMeetings = daysWithMeetings.contains(date)
                            val isToday = date == LocalDate.now()

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .pointerInput(date) {
                                        detectTapGestures {
                                            onSelectDay(date)
                                        }
                                    },
                                shape = RoundedCornerShape(8.dp),
                                color = when {
                                    isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    hasMeetings -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else -> Color.Transparent
                                },
                                border = BorderStroke(
                                    width = if (isToday) 2.dp else if (hasMeetings) 1.dp else 0.dp,
                                    color = if (isToday) MaterialTheme.colorScheme.primary
                                    else if (hasMeetings) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    else Color.Transparent
                                ),
                                tonalElevation = 0.dp
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = dayNumber.toString(),
                                        fontWeight = if (hasMeetings || isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isToday -> MaterialTheme.colorScheme.primary
                                            hasMeetings -> MaterialTheme.colorScheme.primary
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                    if (hasMeetings) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedMeetingCard(
    meeting: MeetingDto,
    formattedTime: String
) {
    val statusColor = when (meeting.status) {
        "SCHEDULED", "PLANNED" -> MaterialTheme.colorScheme.primary
        "COMPLETED" -> MaterialTheme.colorScheme.tertiary
        "CANCELLED" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.secondary
    }

    val priorityColor = when (meeting.meetingPriority) {
        "HIGH" -> MaterialTheme.colorScheme.error
        "MEDIUM" -> MaterialTheme.colorScheme.primary
        "LOW" -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(statusColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = meeting.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "Время",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(priorityColor.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = when (meeting.meetingPriority) {
                                "HIGH" -> "Высокий"
                                "MEDIUM" -> "Средний"
                                "LOW" -> "Низкий"
                                else -> meeting.meetingPriority
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = priorityColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                meeting.description?.takeIf { it.isNotBlank() }?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = when (meeting.status) {
                            "SCHEDULED", "PLANNED" -> "Запланировано"
                            "COMPLETED" -> "Завершено"
                            "CANCELLED" -> "Отменено"
                            else -> meeting.status
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private fun parseMeetingDateTime(meeting: MeetingDto): Pair<LocalDateTime, LocalDateTime>? {
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm"
    ).map { DateTimeFormatter.ofPattern(it) }

    fun parse(value: String): LocalDateTime? {
        for (formatter in patterns) {
            try {
                return LocalDateTime.parse(value, formatter)
            } catch (_: DateTimeParseException) {
            }
        }
        return null
    }

    val start = parse(meeting.startTime)
    val end = parse(meeting.endTime)
    if (start != null && end != null) return start to end
    return null
}

private fun formatMeetingTime(start: LocalDateTime, end: LocalDateTime): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return "${start.format(timeFormatter)} - ${end.format(timeFormatter)}"
}