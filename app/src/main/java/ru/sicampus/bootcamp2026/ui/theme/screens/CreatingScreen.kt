package ru.sicampus.bootcamp2026.ui.theme.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CreatingScreen(
    val id: String = "",
    val title: String,
    val dateTime: LocalDateTime,
    val location: String? = null,
    val description: String = "",
    val participants: List<String> = emptyList()
)


data class FreeSlot(val time: String)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMeetingScreen(
    onMeetingCreated: (CreatingScreen) -> Unit,
    onCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var title by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf(30) } // по умолчанию 30 мин
    var location by remember { mutableStateOf("Кабинет 31") }
    val freeSlots = remember { generateFreeSlots() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Создание встречи",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Тема встречи") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Продолжительность",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                DurationPicker(
                    selectedDuration = duration,
                    onDurationSelected = { duration = it }
                )
                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Место") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "Свободные слоты",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                FreeSlotsGrid(slots = freeSlots)

                Spacer(modifier = Modifier.height(24.dp))


                OutlinedButton(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0057B8)
                    )

                ) {
                    Text(text = "Пригласить ещё")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {

                        val now = LocalDateTime.now()
                        val meeting = CreatingScreen(
                            title = title.ifEmpty { "Без темы" },
                            dateTime = now.plusMinutes(duration.toLong()),
                            location = location,
                            participants = emptyList(),
                            description = "Создана через приложение"
                        )
                        onMeetingCreated(meeting)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0057B8)
                    )
                ) {
                    Text(text = "Создать встречу")
                }
            }
        }


        TextButton(
            onClick = onCancel,
            modifier = Modifier.padding(top = 16.dp) ,
            colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0057B8)
                    )
        ) {
            Text(text = "Отмена")
        }
    }
}



@Composable
private fun DurationPicker(
    selectedDuration: Int,
    onDurationSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        listOf(15, 30, 45, 60).forEach { mins ->
            FilterChip(
                selected = mins == selectedDuration,
                onClick = { onDurationSelected(mins) },
                label = { Text("$mins мин") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FreeSlotsGrid(slots: List<FreeSlot>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(200.dp)
    ) {
        items(slots) { slot ->
            FreeSlotItem(slot = slot)
        }
    }
}

@Composable
private fun FreeSlotItem(slot: FreeSlot) {
    Card(
        modifier = Modifier.size(64.dp),
        onClick = {  }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = slot.time,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
 fun generateFreeSlots(): List<FreeSlot> {
    val slots = mutableListOf<FreeSlot>()
    val start = LocalDateTime.now().withHour(9).withMinute(0)
    val end = LocalDateTime.now().withHour(18).withMinute(0)

    var current = start
    while (current.isBefore(end)) {
        slots.add(FreeSlot(current.format(DateTimeFormatter.ofPattern("HH:mm"))))
        current = current.plusMinutes(30)
    }

    return slots.take(12)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewCreateMeetingScreen() {
    MaterialTheme {
        CreateMeetingScreen(
            onMeetingCreated = {},
            onCancel = {}
        )
    }
}
