package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Notifications(
    modifier: Modifier = Modifier,
    events: List<String> = listOf("Собрание","Обсуждение","Совещание","Собрание","Обсуждение","Совещание","Собрание","Обсуждение","Совещание","Собрание","Обсуждение","Совещание")
    ) {
    var itemsToShow by remember { mutableIntStateOf(5) }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(count = itemsToShow) { index ->
            val event = events[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = event)
                }
            }
        }

        if (itemsToShow < events.size) {
            item {
                Button(
                    onClick = {
                        itemsToShow = (itemsToShow + 5).coerceAtMost(events.size)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Показать ещё")
                }
            }
        }
    }
}