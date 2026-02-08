package ru.sicampus.bootcamp2026.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.MeetingDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//private fun getDefaultMeetings(): List<MeetingDto> = listOf(
//    MeetingDto(
//        id = 1,
//        title = "Ревью кода",
//        startTime = "2026-02-08",
//        endTime = "10:00",
//        members = 3,
//        confirmed = true
//    ),
//    MeetingDto(
//        id = 2,
//        title = "Обсуждение задач",
//        startTime = "2026-02-08",
//        endTime = "14:00",
//        members = 5,
//        confirmed = true
//    ),
//    MeetingDto(
//        id = 3,
//        title = "Планерка команды",
//        startTime = "2026-02-08",
//        endTime = "16:00",
//        members = 8,
//        confirmed = true
//    )
//)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(navController: NavController, userRepository: UserRepository? = null) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var meetings by remember { mutableStateOf(emptyList<MeetingDto>()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

    val datePickerState = rememberDatePickerState()

    LaunchedEffect(selectedDate) {
        if (selectedDate != null && userRepository != null) {
            isLoading = true
            errorMessage = ""
            val dateString = dateFormat.format(Date(selectedDate!!))

            coroutineScope.launch {
                val result = userRepository.getMeetingsByDate(dateString)
                result.onSuccess { meetingsList ->
                    meetings = meetingsList
                }.onFailure { exception ->
                    errorMessage = "Ошибка загрузки: ${exception.message}"
                    meetings = emptyList()
                }
                isLoading = false
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(16.dp))

            // DatePicker кнопка
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (selectedDate != null) {
                        "Выбрана дата: ${displayDateFormat.format(Date(selectedDate!!))}"
                    } else {
                        "📅 Выберите дату"
                    },
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isLoading) {
                Text(
                    text = "Загрузка расписания...",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 16.sp
                )
            } else if (meetings.isNotEmpty()) {
                Text(
                    text = if (selectedDate != null) "Мероприятия на выбранную дату" else "Мероприятия на сегодня",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    meetings.forEach { meeting ->
                        ScheduleCard(
                            title = meeting.title,
                            time = meeting.endTime
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Расписание",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Выберите дату для просмотра",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ScheduleCard(
    title: String,
    time: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⏰", modifier = Modifier.size(14.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(4.dp))
                        Text(time, fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("👤", modifier = Modifier.size(14.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(4.dp))
                            //Text(members, fontSize = 11.sp)
                    }
                }
            }
//            if (confirmed) {
//                Surface(
//                    shape = RoundedCornerShape(20.dp),
//                    color = Color(0xFFCCFCD1)
//                ) {
//                    Text(
//                        text = "Подтверждено",
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                        fontSize = 11.sp,
//                        color = Color(0xFF0A6E20)
//                    )
//                }
//            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Text("📅") },
            label = { Text("Расписание") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("meetings") {
                    popUpTo("meetings") { inclusive = true }
                }
            },
            icon = { Text("✉️") },
            label = { Text("Приглашения") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("profile")
            },
            icon = { Text("👤") },
            label = { Text("Профиль") }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MainHomePreview() {
    val navController = rememberNavController()
    MaterialTheme {
        MainHomeScreen(navController)
    }
}
