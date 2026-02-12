package com.example.frontend.presentation.screens.createmeetup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.data.model.PersonWithInvitesDTO
import com.example.frontend.presentation.viewmodel.createmeetup.CreateMeetupViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetupScreen(
    currentUserId: Long,
    onMeetupCreated: () -> Unit,
    viewModel: CreateMeetupViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf(LocalTime.now()) }
    var search by remember { mutableStateOf("") }
    var showDate by remember { mutableStateOf(false) }
    var showTime by remember { mutableStateOf(false) }
    
    LaunchedEffect(state.createdMeetup) {
        if (state.createdMeetup != null) {
            onMeetupCreated()
            viewModel.clearCreatedMeetup()
        }
    }
    
    val dateState = rememberDatePickerState()
    val timeState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute
    )
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Создать митап") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val df = DateTimeFormatter.ISO_DATE
                    val tf = DateTimeFormatter.ofPattern("HH:mm")
                    viewModel.createMeetupWithInvites(
                        date.format(df),
                        time.format(tf),
                        currentUserId
                    )
                },
                icon = { Icon(Icons.Default.Check, contentDescription = null) },
                text = { Text("Создать") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                CreateContent(
                    date = date,
                    time = time,
                    search = search,
                    users = state.availableUsers,
                    selected = state.selectedUserIds,
                    onSearchChange = { search = it },
                    onToggle = { viewModel.toggleUserSelection(it) },
                    onDateClick = { showDate = true },
                    onTimeClick = { showTime = true }
                )
            }
        }
    }
    
    if (showDate) {
        DatePickerDialog(
            onDismissRequest = { showDate = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = dateState.selectedDateMillis
                    if (millis != null) {
                        date = LocalDate.ofEpochDay(millis / 86400000)
                    }
                    showDate = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDate = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
    
    if (showTime) {
        AlertDialog(
            onDismissRequest = { showTime = false },
            confirmButton = {
                TextButton(onClick = {
                    time = LocalTime.of(timeState.hour, timeState.minute)
                    showTime = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTime = false }) {
                    Text("Отмена")
                }
            },
            text = {
                TimePicker(state = timeState)
            }
        )
    }
}

@Composable
fun CreateContent(
    date: LocalDate,
    time: LocalTime,
    search: String,
    users: List<PersonWithInvitesDTO>,
    selected: Set<Long>,
    onSearchChange: (String) -> Unit,
    onToggle: (Long) -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Дата и время",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    val df = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    OutlinedButton(
                        onClick = onDateClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Дата: ${date.format(df)}")
                    }
                    
                    val tf = DateTimeFormatter.ofPattern("HH:mm")
                    OutlinedButton(
                        onClick = onTimeClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Время: ${time.format(tf)}")
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Пригласить участников (${selected.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item {
            OutlinedTextField(
                value = search,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Поиск по имени или отделу") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (search.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true
            )
        }
        
        item {
            if (selected.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Выбрано:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selected.forEach { uid ->
                            val u = users.find { it.id == uid }
                            if (u != null) {
                                Chip(user = u, onRemove = { onToggle(uid) })
                            }
                        }
                    }
                }
            }
        }
        
        // фильтрует юзеров по поиску
        val filtered = if (search.isEmpty()) {
            users
        } else {
            users.filter {
                it.name.contains(search, ignoreCase = true) ||
                (it.dept?.contains(search, ignoreCase = true) == true)
            }
        }
        
        if (filtered.isEmpty()) {
            item {
                Text(
                    text = "Пользователи не найдены",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            items(filtered) { user ->
                val sel = selected.contains(user.id)
                UserCard(user = user, isSelected = sel, onToggle = { onToggle(user.id) })
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun Chip(user: PersonWithInvitesDTO, onRemove: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(16.dp).clickable(onClick = onRemove),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun UserCard(user: PersonWithInvitesDTO, isSelected: Boolean, onToggle: () -> Unit) {
    val bg = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (user.dept != null) {
                    Text(
                        text = user.dept,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Checkbox(checked = isSelected, onCheckedChange = { onToggle() })
        }
    }
}
