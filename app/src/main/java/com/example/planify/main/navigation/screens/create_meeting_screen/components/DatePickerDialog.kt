package com.example.planify.main.navigation.screens.create_meeting_screen.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    opened: Boolean,
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    zoneId: ZoneId = ZoneId.systemDefault()
) {
    if (!opened) return

    val initialMillis = initialDate
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    val pickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = pickerState.selectedDateMillis != null,
                onClick = {
                    val millis = pickerState.selectedDateMillis ?: return@TextButton
                    val picked = Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
                    onConfirm(picked)
                }
            ) { Text("ОК") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = pickerState)
    }
}

