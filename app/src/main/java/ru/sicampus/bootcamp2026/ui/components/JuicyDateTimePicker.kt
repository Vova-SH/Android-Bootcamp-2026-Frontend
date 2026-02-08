package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuicyDateTimePicker(
    label: String,
    isoValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var tempDate by remember { mutableStateOf<LocalDate?>(null) }

    val displayFormatter = remember { DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm") }
    val isoFormatter = remember { DateTimeFormatter.ISO_LOCAL_DATE_TIME }

    val displayText = remember(isoValue) {
        try {
            val dateTime = LocalDateTime.parse(isoValue, isoFormatter)
            dateTime.format(displayFormatter)
        } catch (e: Exception) {
            "Выберите дату"
        }
    }

    Box(modifier = modifier) {
        JuicyTextField(
            value = displayText,
            onValueChange = {},
            label = label,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showDatePicker = true },
            leadingIcon = { Icon(Icons.Rounded.DateRange, null) },
            readOnly = true
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        tempDate = Instant.ofEpochMilli(selectedMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        showDatePicker = false
                        showTimePicker = true
                    }
                }) { Text("Далее") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = 12,
            initialMinute = 0,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    if (tempDate != null) {
                        val finalDateTime = LocalDateTime.of(tempDate, time)
                        val isoString = finalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                        onValueChange(isoString)
                    }
                    showTimePicker = false
                }) { Text("Готово") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Отмена") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}