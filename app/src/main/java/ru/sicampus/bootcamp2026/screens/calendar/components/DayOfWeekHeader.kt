package ru.sicampus.bootcamp2026.screens.calendar.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DayOfWeekHeader(
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf("ВС", "ПН", "ВТ", "СР", "ЧТ", "ПТ", "Сб")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        daysOfWeek.forEach { day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun DayOfWeekHeaderPreview() {
    MaterialTheme {
        DayOfWeekHeader()
    }
}