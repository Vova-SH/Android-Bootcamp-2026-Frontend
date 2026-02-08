package ru.sicampus.bootcamp2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.components.JuicyBackground
import ru.sicampus.bootcamp2026.ui.components.JuicyButton
import ru.sicampus.bootcamp2026.ui.components.JuicyDateTimePicker
import ru.sicampus.bootcamp2026.ui.components.JuicyTextField
import ru.sicampus.bootcamp2026.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateMeetingScreen(
    onCreateClick: (String, String, String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }

    var dateIso by remember {
        mutableStateOf(
            LocalDateTime.now().plusHours(1)
                .withSecond(0).withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        )
    }

    var duration by remember { mutableStateOf("60") }

    JuicyBackground {
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text("Новая встреча", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("Заполните детали", color = TextSecondary)

                Spacer(modifier = Modifier.height(32.dp))

                JuicyTextField(value = title, onValueChange = { title = it }, label = "Название", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))

                JuicyTextField(value = description, onValueChange = { description = it }, label = "Описание", modifier = Modifier.fillMaxWidth(), singleLine = false)
                Spacer(modifier = Modifier.height(16.dp))

                JuicyTextField(value = place, onValueChange = { place = it }, label = "Место", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))

                JuicyDateTimePicker(
                    label = "Дата и время начала",
                    isoValue = dateIso,
                    onValueChange = { newIso -> dateIso = newIso },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                JuicyTextField(
                    value = duration,
                    onValueChange = { if (it.all { char -> char.isDigit() }) duration = it },
                    label = "Длительность (мин)",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                JuicyButton(
                    onClick = {
                        val dur = duration.toIntOrNull() ?: 60
                        onCreateClick(title, description, place, dateIso, dur)
                    },
                    text = "Создать",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = title.isNotBlank() && place.isNotBlank() && duration.isNotBlank()
                )
            }
        }
    }
}