package ru.sicampus.bootcamp2026.presentation.ui.screens.main.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

data class User(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)

@Composable
fun MeetCreateScreen(nav: NavHostController) {
    MeetCreateContent(
        onBackClick = { nav.popBackStack() },
        onCreateClick = { /* TODO */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeetCreateContent(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var purpose by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var selectedTime by rememberSaveable { mutableStateOf("") }

    var timeExpanded by remember { mutableStateOf(false) }
    var usersExpanded by remember { mutableStateOf(false) }

    // Список доступного времени
    val times = listOf(
        "09:00", "10:00", "11:00",
        "12:00", "13:00", "14:00",
        "15:00", "16:00", "17:00", "18:00"
    )

    // Список пользователей
    var availableUsers by remember {
        mutableStateOf(
            listOf(
                User("1", "User11"),
                User("2", "User12121"),
                User("3", "User123"),
                User("4", "Ivan"),
                User("5", "Maria")
            )
        )
    }

    val selectedUsers = availableUsers.filter { it.isSelected }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Создать встречу") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Button(
                onClick = onCreateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Добавить", style = MaterialTheme.typography.labelLarge)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Описание")

            CreateTextField(
                value = name,
                onValueChange = { name = it },
                label = "Название встречи",
                leadingIcon = Icons.Default.Edit
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateTextField(
                value = purpose,
                onValueChange = { purpose = it },
                label = "Цель встречи",
                leadingIcon = null
            )

            Spacer(modifier = Modifier.height(12.dp))

            CreateTextField(
                value = description,
                onValueChange = { description = it },
                label = "Описание встречи",
                leadingIcon = null,
                isMultiLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Дата встречи")
            CreateTextField(
                value = date,
                onValueChange = { date = it },
                label = "ГГ.ММ.ДД",
                leadingIcon = Icons.Default.CalendarToday
            )

            Spacer(modifier = Modifier.height(12.dp))

            //Поле для выбора времени
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedTime,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Выберите время") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AccessTime,
                            null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { timeExpanded = !timeExpanded }) {
                            Icon(
                                if (timeExpanded) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                                null
                            )
                        }
                    },
                    readOnly = true,
                    enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { timeExpanded = true }
                )

                DropdownMenu(
                    expanded = timeExpanded,
                    onDismissRequest = { timeExpanded = false },
                    modifier = Modifier
                        .width(200.dp)
                        .heightIn(max = 300.dp),
                    properties = PopupProperties(focusable = false)
                ) {
                    times.forEach { time ->
                        DropdownMenuItem(
                            text = { Text(time) },
                            onClick = {
                                selectedTime = time
                                timeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Пригласить")
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = if (selectedUsers.isEmpty()) ""
                    else "${selectedUsers.size} выбрано",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Выберите участников") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.PersonAdd,
                            null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { usersExpanded = !usersExpanded }) {
                            Icon(
                                if (usersExpanded) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                                null
                            )
                        }
                    },
                    readOnly = true,
                    enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { usersExpanded = true }
                )

                DropdownMenu(
                    expanded = usersExpanded,
                    onDismissRequest = { usersExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    properties = PopupProperties(focusable = false)
                ) {
                    availableUsers.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.name) },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (user.isSelected)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (user.isSelected)
                                            Icons.Default.Check
                                        else
                                            Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (user.isSelected)
                                            MaterialTheme.colorScheme.onPrimary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                availableUsers = availableUsers.map {
                                    if (it.id == user.id)
                                        it.copy(isSelected = !it.isSelected)
                                    else
                                        it
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список выбранных участников
            if (selectedUsers.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Приглашённые лица",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                selectedUsers.forEach { user ->
                    SelectedUserChip(
                        user = user,
                        onRemove = {
                            availableUsers = availableUsers.map {
                                if (it.id == user.id) it.copy(isSelected = false) else it
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun CreateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector?,
    isMultiLine: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = leadingIcon?.let {
            { Icon(it, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
        },
        singleLine = !isMultiLine,
        minLines = if (isMultiLine) 3 else 1,
        maxLines = if (isMultiLine) 5 else 1,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun SelectedUserChip(
    user: User,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyLarge
        )

        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Удалить",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MeetCreateScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        MeetCreateContent(
            onBackClick = {},
            onCreateClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MeetCreateScreenDarkPreview() {
    AndroidBootcamp2026FrontendTheme {
        MeetCreateContent(
            onBackClick = {},
            onCreateClick = {}
        )
    }
}