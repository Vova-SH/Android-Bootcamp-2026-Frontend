package ru.sicampus.bootcamp2026.ui.theme.screens




import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




data class DataScreen(
    val id: String = "",
    val title: String,
    val location: String? = null,
    val description: String = "",
    val participants: List<String> = emptyList(),
    val dateTime: LocalDateTime
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingDetailsScreen(
    meeting: DataScreen,
    onEditTimeClick: () -> Unit,
    onAddParticipantClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Данные о встрече",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                DetailItem(label = "Тема", value = meeting.title)

                Spacer(modifier = Modifier.height(12.dp))

                val dateTimeStr = meeting.dateTime.format(
                    DateTimeFormatter.ofPattern("dd MMMM yyyy — HH:mm")
                )
                DetailItem(label = "Дата", value = dateTimeStr)

                Spacer(modifier = Modifier.height(12.dp))


                DetailItem(
                    label = "Место",
                    value = meeting.location ?: "Кабинет 231"
                )

                Spacer(modifier = Modifier.height(12.dp))


                Text(
                    text = "Участники",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (meeting.participants.isEmpty()) {
                    Text(
                        text = "Пока нет участников",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    meeting.participants.forEach { participant ->
                        ParticipantChip(participant)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "Действия",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )


                FilledTonalButton(
                    onClick = onEditTimeClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0057B8)
                    )
                ) {
                    Text("Изменить время")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onAddParticipantClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0057B8)
                    )
                ) {
                    Text("Добавить участника")
                }


                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDeclineClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0057B8)
                    )

                ) {
                    Text("Отказаться")
                }
            }
        }
    }
}



@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ParticipantChip(name: String) {
    Card(
        modifier = Modifier
            .height(36.dp)
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Участник",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMeetingDetailsScreen() {
    MaterialTheme {
        MeetingDetailsScreen(
            meeting = DataScreen(
                title = "Планирование спринта",
                dateTime = LocalDateTime.now(),
                location = "Конф. зал А",
                participants = listOf("Иван Иванов", "Мария Петрова", "Алексей Сидоров")
            ),
            onEditTimeClick = {},
            onAddParticipantClick = {},
            onDeclineClick = {}
        )
    }
}

