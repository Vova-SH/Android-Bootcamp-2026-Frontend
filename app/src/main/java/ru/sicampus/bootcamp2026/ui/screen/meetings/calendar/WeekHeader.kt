package ru.sicampus.bootcamp2026.ui.screen.meetings.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.time.format.TextStyle
import java.util.Locale
import java.time.DayOfWeek
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.PrimaryGray
import java.time.LocalDate

@Composable
fun WeekHeader(
    selectedDate: LocalDate
) {
    val daysOfWeek = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach { dayOfWeek ->
            val isSelected = selectedDate.dayOfWeek == dayOfWeek
            val textColor =
                if (isSelected) Black
                else PrimaryGray

            Text(
                text = dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ),
                color = textColor,
                modifier = Modifier
                    .width(36.dp),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}