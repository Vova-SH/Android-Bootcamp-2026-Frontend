package com.example.create_meet.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.comon.UserDto

@Composable
fun AddMeetingScreen(
    addMeetingViewModel: AddMeetingViewModel,
    onMeetingCreated: () -> Unit
) {
    val vm = addMeetingViewModel

    if (vm.submitSuccess) {
        LaunchedEffect(Unit) {
            onMeetingCreated()

        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Создать встречу", style = MaterialTheme.typography.titleLarge)
        }

        item {
            OutlinedTextField(
                value = vm.title,
                onValueChange = { vm.title = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = vm.description,
                onValueChange = { vm.description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = vm.location,
                onValueChange = { vm.location = it },
                label = { Text("Место") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = vm.dateInput,
                onValueChange = { vm.dateInput = it },
                label = { Text("Дата (yyyy‑MM‑dd)") },
                placeholder = { Text("2026‑02‑14") },
                isError = vm.dateError != null,
                modifier = Modifier.fillMaxWidth()
            )
            vm.dateError?.let {
                Text(it, color = Color.Red)
            }
        }

        item {
            OutlinedTextField(
                value = vm.timeInput,
                onValueChange = { vm.timeInput = it },
                label = { Text("Время (HH:mm)") },
                placeholder = { Text("14:00") },
                isError = vm.timeError != null,
                modifier = Modifier.fillMaxWidth()
            )
            vm.timeError?.let {
                Text(it, color = Color.Red)
            }
        }

        item {
            OutlinedTextField(
                value = vm.durationHours.toString(),
                onValueChange = { vm.durationHours = it.toIntOrNull() ?: 1 },
                label = { Text("Длительность (часы)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            if (vm.usersLoading) {
                CircularProgressIndicator()
            } else if (vm.usersError != null) {
                Text("Ошибка: ${vm.usersError}", color = Color.Red)
            } else {

                UsersMultiSelectDropdown(
                    users = vm.users,
                    selected = vm.selectedUsers,
                    onSelectionChanged = { vm.selectedUsers = it },
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            vm.submitError?.let {
                Text("Ошибка: $it", color = Color.Red)
            }
        }

        item {
            Button(
                onClick = { vm.onSubmit() },
                enabled = !vm.isSubmitting && vm.title.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (vm.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text("Создать")
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun UsersMultiSelectDropdown(
    users: List<UserDto>,
    selected: List<String>,
    onSelectionChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Пригласить участников"
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {

        OutlinedTextField(
            value = if (selected.isEmpty())
                label
            else
                "$label (${selected.size})",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            users.forEach { user ->
                val isSelected = selected.contains(user.id)

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(user.fullName)
                        }
                    },
                    onClick = {
                        val newSelected = if (isSelected) {
                            selected - user.id
                        } else {
                            selected + user.id
                        }
                        onSelectionChanged(newSelected)
                    }
                )
            }
        }
    }
}
