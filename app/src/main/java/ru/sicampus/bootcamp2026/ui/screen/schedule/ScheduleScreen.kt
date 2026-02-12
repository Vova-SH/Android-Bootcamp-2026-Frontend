package ru.sicampus.bootcamp2026.ui.screen.schedule

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.navigation.NavBar
import ru.sicampus.bootcamp2026.navigation.Routes
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.components.SimpleButton
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    navController: NavHostController,
    viewModel: ScheduleStateModel = viewModel<ScheduleStateModel>()
) {
    val state by viewModel.uiState.collectAsState()
    
    ScheduleScreenContent(
        state = state,
        navController = navController,
        onRefresh = { viewModel.getData() },
        onDateSelected = { viewModel.onDateSelected(it) }
    )
}

@Composable
private fun ScheduleScreenContent(
    state: ScheduleState,
    navController: NavHostController,
    onRefresh: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    when (state) {
        is ScheduleState.Loading -> ScheduleStateLoading()
        is ScheduleState.Error -> ScheduleStateError(state, onRefresh = onRefresh)
        is ScheduleState.Content -> ScheduleStateContent(
            state = state,
            navController = navController,
            onDateSelected = onDateSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleStateContent(
    state: ScheduleState.Content,
    navController: NavHostController,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedTab by remember { mutableStateOf("день") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расписание",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Invitations.route) }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Приглашения",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        navController.navigate(Routes.Auth.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Выйти",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("день", "неделя", "месяц").forEach { title ->
                    TabItem(title, selectedTab == title) { selectedTab = title }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(top = 24.dp)
            ) {
                when (selectedTab) {
                    "месяц" -> MonthCalendarView(
                        selectedDate = state.selectedDate,
                        onDateSelected = onDateSelected,
                        meetings = state.meetings
                    )
                    "неделя" -> WeekCalendarView(
                        selectedDate = state.selectedDate,
                        onDateSelected = onDateSelected,
                        meetings = state.meetings
                    )
                    "день" -> DayCalendarView(
                        selectedDate = state.selectedDate,
                        meetings = state.meetings
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun MonthCalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    meetings: List<ru.sicampus.bootcamp2026.domain.entities.MeetingEntity>
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Предыдущий месяц")
            }
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))).replaceFirstChar { it.uppercase() },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Следующий месяц")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val firstDayOfMonth = currentMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value 
        val daysInMonth = currentMonth.lengthOfMonth()
        
        val calendarDays = mutableListOf<LocalDate?>()
        for (i in 1 until firstDayOfWeek) calendarDays.add(null)
        for (i in 1..daysInMonth) calendarDays.add(currentMonth.atDay(i))
        while (calendarDays.size % 7 != 0) calendarDays.add(null)

        Column(modifier = Modifier.fillMaxWidth()) {
            calendarDays.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        if (date != null) {
                            val isSelected = date == selectedDate
                            val hasMeetings = meetings.any { it.date == date }
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary 
                                        else if (date == LocalDate.now()) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                        else Color.Transparent
                                    )
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (hasMeetings && !isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Встречи на ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        val filteredMeetings = meetings.filter { it.date == selectedDate }
        if (filteredMeetings.isEmpty()) {
            Text("Встреч нет", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                filteredMeetings.forEach { meeting ->
                    ScheduleCard(
                        title = meeting.title,
                        organizer = meeting.organizerId.toString(),
                        date = meeting.date.toString(),
                        time = "${meeting.startHour}:00"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@SuppressLint("NewApi")
@Composable
fun WeekCalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    meetings: List<ru.sicampus.bootcamp2026.domain.entities.MeetingEntity>
) {
    val startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Неделя: ${startOfWeek.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))} - ${startOfWeek.plusDays(6).format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (i in 0..6) {
                val date = startOfWeek.plusDays(i.toLong())
                val isSelected = date == selectedDate
                val hasMeetings = meetings.any { it.date == date }
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                        .clickable { onDateSelected(date) }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")).uppercase(),
                        fontSize = 10.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                    if (hasMeetings && !isSelected) {
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Встречи на ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val selectedDayMeetings = meetings.filter { it.date == selectedDate }.sortedBy { it.startHour }

        if (selectedDayMeetings.isEmpty()) {
            Text("В этот день встреч нет", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedDayMeetings.forEach { meeting ->
                    ScheduleCard(
                        title = meeting.title,
                        organizer = meeting.organizerId.toString(),
                        date = meeting.date.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru"))),
                        time = "${meeting.startHour}:00"
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun DayCalendarView(
    selectedDate: LocalDate,
    meetings: List<ru.sicampus.bootcamp2026.domain.entities.MeetingEntity>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale("ru"))).replaceFirstChar { it.uppercase() },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val dayMeetings = meetings.filter { it.date == selectedDate }.sortedBy { it.startHour }
        
        if (dayMeetings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("На этот день встреч не запланировано", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            dayMeetings.forEach { meeting ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = String.format("%02d:00", meeting.startHour),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    ScheduleCard(
                        title = meeting.title,
                        organizer = meeting.organizerId.toString(),
                        date = "",
                        time = "${meeting.startHour}:00"
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleStateError(
    state: ScheduleState.Error,
    onRefresh: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ошибка: ${state.reason}", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            SimpleButton("Повторить") {
                onRefresh()
            }
        }
    }
}

@Composable
private fun ScheduleStateLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun ScheduleCard(title: String, organizer: String, date: String, time: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title, 
                fontSize = 20.sp, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Организатор: $organizer", 
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            if (date.isNotEmpty()) {
                Text(
                    text = "Дата: $date", 
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Время: $time", 
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScheduleScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        ScheduleScreenContent(
            state = ScheduleState.Content(),
            navController = rememberNavController(),
            onRefresh = {},
            onDateSelected = {}
        )
    }
}
