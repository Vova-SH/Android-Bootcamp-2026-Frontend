package ru.sicampus.bootcamp2026.ui.theme.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingCard(
    meeting: DataScreen,
    onDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = meeting.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = meeting.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                style = MaterialTheme.typography.bodyMedium
            )

            if (meeting.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = meeting.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = onDetailsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0057B8)
                )
            ) {
                Text(text = "Узнать больше")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingsScreen(
    meetings: List<DataScreen>,
    onMeetingClick: (DataScreen) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Мои встречи",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )


        LazyColumn {
            items(meetings) { meeting ->
                MeetingCard(
                    meeting = meeting,
                    onDetailsClick = { onMeetingClick(meeting) }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMeetingsScreen() {
    MaterialTheme {
        MeetingsScreen(
            meetings = listOf(
                DataScreen(
                    title = "Встреча с командой",
                    dateTime = LocalDateTime.now(),
                    description = "Обсуждение плана разработки на следующий спринт"
                )
            ),
            onMeetingClick = {}
        )
    }
}
