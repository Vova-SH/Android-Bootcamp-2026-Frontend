package ru.sicampus.bootcamp2026.ui.screens.timetable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import ru.sicampus.bootcamp2026.ui.theme.accentBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimeTable (
    modifier: Modifier = Modifier,
    events: List<String> = listOf("Собрание","Обсуждение","Совещание","Собрание","Обсуждение","Совещание","Собрание","Обсуждение","Совещание","Собрание","Обсуждение","Совещание")){
    // открыть/закрыть окно
    var showDialog by remember { mutableStateOf(false) }
    // диапазон дат
    var selectedRange by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }

    val dateText = if (selectedRange.first != null && selectedRange.second != null) {
        val formatter = java.text.SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val start = selectedRange.first?.let { formatter.format(it) } ?: ""
        val end = selectedRange.second?.let { formatter.format(it) } ?: ""
        "$start - $end"
    } else {
        "Выбрать дату"
    }
    var itemsToShow by remember { mutableStateOf(5) }

    Column(Modifier.fillMaxSize().padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, accentBlue),
        ) {
            Text(dateText)
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Диалог выбора даты
        if (showDialog) {
            DateRangePickerModal(
                onDateRangeSelected = { range ->
                    selectedRange = range
                    showDialog = false
                    itemsToShow = 5
                },
                onDismiss = { showDialog = false }
            )
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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

            /*Box(modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(elevation = 4.dp)
                .background(MaterialTheme.colorScheme.surface)
                ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }*/
    }

}

@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Выберите дату"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

/** скоприровал с документации от гугла пока не убирай пж **/
/*@Composable
fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}*/


/*fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}*/

@Preview
@Composable
fun SeeTimeTable() {
    TimeTable()
}