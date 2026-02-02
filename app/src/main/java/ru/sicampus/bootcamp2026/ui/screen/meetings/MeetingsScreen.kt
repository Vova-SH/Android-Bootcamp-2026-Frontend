package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.domain.model.Meeting

@Composable
fun MeetingsScreen(
    modifier: Modifier = Modifier,
    viewModel: MeetingsViewModel = viewModel()
) {
    val meetings by viewModel.meetings.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(meetings) { meeting ->
            MeetingCard(meeting)
        }
    }
}

@Composable
fun MeetingCard(meeting: Meeting) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meeting.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Где: ${meeting.place}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Когда: ${meeting.time}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Длительность: ${meeting.duration}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}