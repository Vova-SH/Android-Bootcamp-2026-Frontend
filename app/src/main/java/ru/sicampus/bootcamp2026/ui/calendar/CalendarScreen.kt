package ru.sicampus.bootcamp2026.ui.calendar

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.components.HomeMeetingCard
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

val GreenLight = Color(0xFFBBDBA6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val datePickerState = rememberDatePickerState()
    var isCalendarExpanded by remember { mutableStateOf(true) }

    //Вычисление выбранной даты
    val selectedDate: LocalDate? = datePickerState.selectedDateMillis?.let { millis ->
        Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
    }

    //Авто-скрытие при выборе
    LaunchedEffect(datePickerState.selectedDateMillis) {
        if (datePickerState.selectedDateMillis != null) {
            isCalendarExpanded = false
        }
    }

    //тестовые данные
    val allMeetings = remember {
        val today = LocalDate.now()
        listOf(
            MockMeeting("Meeting A", today, "10:00"),
            MockMeeting("Project B", today, "14:00"),
            MockMeeting("Meeting C", today, "15:00"),
            MockMeeting("Project D", today, "16:00"),
            MockMeeting("Design Review", today.plusDays(1), "11:00")
        )
    }

    val filteredMeetings = if (selectedDate != null) {
        allMeetings.filter { it.date == selectedDate }
    } else {
        emptyList()
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .statusBarsPadding()
    ) {

        //Заголовок (Виден, когда календарь СВЕРНУТ)
        //Показываем всегда, если календарь закрыт (даже если дата null)
        AnimatedVisibility(
            visible = !isCalendarExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Surface(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { isCalendarExpanded = true }
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (selectedDate == null) "Action required" else "Selected Date",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                        //Если дата не выбрана, пишем "Select Date"
                        Text(
                            text = selectedDate?.format(dateFormatter) ?: "Select a date",
                            style = MaterialTheme.typography.titleLarge,
                            color = GreenLight,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Кнопка открытия
                    IconButton(
                        onClick = { isCalendarExpanded = true },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = GreenLight)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Edit", tint = Color.Black)
                    }
                }
            }
        }

        //Календарь (Виден, когда РАЗВЕРНУТ)
        AnimatedVisibility(
            visible = isCalendarExpanded,
            //Плавное открытие (разворачивание сверху вниз + прозрачность)
            enter = expandVertically(
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Top
            ) + fadeIn(
                animationSpec = tween(durationMillis = 500)
            ),
            //Плавное закрытие (сворачивание вверх + исчезновение)
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Top
            ) + fadeOut(
                animationSpec = tween(durationMillis = 450)
            )
        ) {
            Surface(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(48.dp),
                modifier = Modifier.width(420.dp).padding(16.dp).align(Alignment.CenterHorizontally)
            ) {
                Column {
                    MaterialTheme(
                        colorScheme = MaterialTheme.colorScheme.copy(
                            onSurface = Color.White,
                            onSurfaceVariant = Color.White.copy(alpha = 0.6f),
                            primary = GreenLight,
                            surface = Color.Transparent
                        )
                    ) {
                        DatePicker(
                            state = datePickerState,
                            modifier = Modifier.padding(16.dp),
                            colors = DatePickerDefaults.colors(
                                containerColor = Color.Transparent,
                                titleContentColor = GreenLight,
                                headlineContentColor = Color.White,
                                weekdayContentColor = GreenLight,
                                subheadContentColor = Color.White,
                                yearContentColor = GreenLight,
                                currentYearContentColor = Color.White,
                                selectedYearContentColor = GreenLight,
                                selectedYearContainerColor = GreenLight,
                                dayContentColor = Color.White,
                                selectedDayContainerColor = GreenLight,
                                selectedDayContentColor = Color.Black,
                                todayContentColor = GreenLight,
                                todayDateBorderColor = GreenLight
                            )
                        )
                    }

                    // Кнопка "Свернуть" снизу календаря
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        IconButton(onClick = { isCalendarExpanded = false }) {
                            Icon(Icons.Default.KeyboardArrowUp, null, tint = Color.Gray)
                        }
                    }
                }
            }
        }

        //Список встреч
        // Виден всегда под заголовком/календарем
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (selectedDate == null) {
                if (!isCalendarExpanded) {
                    Text("Please select a date above", color = Color.White,
                        modifier = Modifier
                            .align (Alignment.Center))
                }
            } else if (filteredMeetings.isEmpty()) {
                Text(
                    text = "No meetings found",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredMeetings) { meeting ->
                        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                            HomeMeetingCard(
                                title = meeting.title,
                                date = meeting.date.format(DateTimeFormatter.ofPattern("dd MMM")),
                                startTime = meeting.time,
                                endTime = meeting.time,
                                participantsCount = 3,
                                onClick = { /* ... */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

//Для теста
data class MockMeeting(val title: String, val date: LocalDate, val time: String)

@Composable
@Preview
fun CalendarScreenPreview(){
    CalendarScreen(

    )


}
