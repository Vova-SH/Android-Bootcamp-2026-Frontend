package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateMeetingScreen(
    onCreateClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedHour by remember { mutableStateOf<Int?>(null) }

    // хардкод
    val participants = listOf("Anna", "Boris", "Clara", "Dmitry")
    val timeSlots = (9..17).map { it } // 9:00 to 17:00

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "New Meeting",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Topic") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Select Time Slot (1 hour)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                timeSlots.forEach { hour ->
                    FilterChip(
                        selected = selectedHour == hour,
                        onClick = { selectedHour = hour },
                        label = { Text(text = "$hour:00 - ${hour + 1}:00") }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Participants",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(participants.size) { index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(checked = false, onCheckedChange = {})
                Text(text = participants[index])
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onCreateClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotEmpty() && selectedHour != null
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.padding(4.dp))
                Text("Create Meeting")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateMeetingScreenPreview() {
    CreateMeetingScreen(onCreateClick = {})
}