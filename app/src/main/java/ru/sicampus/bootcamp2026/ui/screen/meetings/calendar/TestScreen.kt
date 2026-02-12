package ru.sicampus.bootcamp2026.ui.screen.meetings.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate

@Preview
@Composable
fun WeekCalendarTestScreen() {
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    Calendar(
        selectedDate = selectedDate,
        onDateSelected = { selectedDate = it }
    )
//    Column() {
//        WeekCalendar(
//            selectedDate = selectedDate,
//            onDateSelected = {
//                selectedDate = it
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = "Selected: $selectedDate"
//        )
//    }
}
