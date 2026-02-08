package ru.sicampus.bootcamp2026.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.dto.InvitationDto
import ru.sicampus.bootcamp2026.data.source.MeetingDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteScreen(navController: NavController, userRepository: UserRepository? = null) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var invitations by remember { mutableStateOf(emptyList<InvitationDto>()) }
    var currentUserId by remember { mutableStateOf(1L) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    val coroutineScope = rememberCoroutineScope()
    val displayDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        if (userRepository != null) {
            isLoading = true
            errorMessage = ""

            coroutineScope.launch {
                val result = userRepository.getInvitationsByPersonId(currentUserId)
                result.onSuccess { invitationsList ->
                    invitations = invitationsList
                }.onFailure { exception ->
                    invitations = emptyList()
                    errorMessage = "Ошибка загрузки приглашений: ${exception.message}"
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
        bottomBar = { BottomNavigation(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Header()

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
            }

            if (isLoading) {
                Text("Загрузка приглашений...")
            } else if (invitations.isNotEmpty()) {
                Text(
                    text = "Мои приглашения",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    invitations.forEach { invitation ->
                        InviteCard(
                            title = "Приглашение от ${invitation.personName}",
                            date = "Встреча #${invitation.meetingId}",
                            status = invitation.status
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Приглашения",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Выберите дату для просмотра",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InviteCard(
    title: String,
    date: String,
    status: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📅", modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(date)
            }
            Spacer(Modifier.height(8.dp))

            // Показываем статус
            Text(
                text = "Статус: $status",
                color = when (status) {
                    "PENDING" -> Color(0xFFF57C00)
                    "ACCEPTED" -> Color(0xFF388E3C)
                    "DECLINED" -> Color(0xFFD32F2F)
                    else -> Color.Gray
                }
            )

            Spacer(Modifier.height(16.dp))

            if (status == "PENDING") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButton(
                        text = "Принять",
                        color = Color(0xFF33C75A),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // TODO: отправить запрос на принятие
                        }
                    )
                    ActionButton(
                        text = "Отклонить",
                        color = Color(0xFFE53935),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // TODO: отправить запрос на отклонение
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(100.dp)
    ) {
        Text(text)
    }
}

@Composable
fun ActionButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(100.dp)
    ) {
        Text(text)
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            },
            icon = { Text("📅") },
            label = { Text("Расписание") }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
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
fun InvitePreview() {
    val navController = rememberNavController()
    MaterialTheme {
        InviteScreen(navController)
    }
}
