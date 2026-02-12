package ru.sicampus.bootcamp2026.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.navigation.NavBar
import ru.sicampus.bootcamp2026.navigation.Routes
import ru.sicampus.bootcamp2026.components.SimpleButton
import ru.sicampus.bootcamp2026.components.SimpleTextField
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainStateModel = viewModel<MainStateModel>()
) {
    val state by viewModel.uiState.collectAsState()
    
    MainScreenContent(
        state = state,
        navController = navController,
        onMeetingNameChange = viewModel::onMeetingNameChange,
        onMeetingDateChange = viewModel::onMeetingDateChange,
        onMeetingTimeChange = viewModel::onMeetingTimeChange,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onUserSelect = viewModel::onUserSelect,
        onUserRemove = viewModel::onUserRemove,
        onCreateMeetingClick = viewModel::onCreateMeetingClick,
        onRefresh = { viewModel.getData() }
    )
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    state: MainState,
    navController: NavHostController,
    onMeetingNameChange: (String) -> Unit,
    onMeetingDateChange: (String) -> Unit,
    onMeetingTimeChange: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onUserSelect: (UserEntity) -> Unit,
    onUserRemove: (Long) -> Unit,
    onCreateMeetingClick: () -> Unit,
    onRefresh: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                return utcTimeMillis >= today
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onMeetingDateChange(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Выберите время") },
            text = {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items((0..23).toList()) { hour ->
                        val time = String.format("%02d:00", hour)
                        Text(
                            text = time,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onMeetingTimeChange(time)
                                    showTimePicker = false
                                }
                                .padding(16.dp),
                            fontSize = 18.sp
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Создание встречи",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Invitations.route) }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Приглашения",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        navController.navigate(Routes.Auth.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Выйти",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavBar(navController = navController)
        },
        modifier = Modifier
            .statusBarsPadding()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(16.dp))

                SimpleTextField(
                    value = state.meetingName,
                    onValueChange = onMeetingNameChange,
                    label = "Название встречи",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                        SimpleTextField(
                            value = state.meetingDate,
                            onValueChange = {},
                            label = "Дата встречи",
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                        Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(modifier = Modifier.fillMaxWidth().clickable { showTimePicker = true }) {
                        SimpleTextField(
                            value = state.meetingTime,
                            onValueChange = {},
                            label = "Время встречи",
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                        Box(modifier = Modifier.matchParentSize().clickable { showTimePicker = true })
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Приглашения",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                SimpleTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = "Поиск пользователей",
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                if (state.foundUsers.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            state.foundUsers.forEach { user ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onUserSelect(user) }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = user.name, fontWeight = FontWeight.Bold)
                                        user.email?.let {
                                            Text(
                                                text = it,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (state.selectedUsers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Выбранные участники:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        state.selectedUsers.forEach { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = user.name, fontWeight = FontWeight.Medium)
                                        user.email?.let {
                                            Text(
                                                text = it,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    IconButton(onClick = { onUserRemove(user.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Удалить",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SimpleButton("Создать встречу") {
                    onCreateMeetingClick()
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (state.isSuccess) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = {  },
                    confirmButton = {
                    SimpleButton("OK") {
                        onRefresh()
                        navController.navigate(Routes.Schedule.route) {
                            popUpTo(Routes.Home.route) { inclusive = true }
                        }
                    }
                },
                    title = { Text("Успех") },
                    text = { Text("Встреча успешно создана!") }
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Ошибка: ${state.error}", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        SimpleButton("Повторить") {
                            onRefresh()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        state = MainState(),
        navController = rememberNavController(),
        onMeetingNameChange = {},
        onMeetingDateChange = {},
        onMeetingTimeChange = {},
        onSearchQueryChange = {},
        onUserSelect = {},
        onUserRemove = {},
        onCreateMeetingClick = {},
        onRefresh = {}
    )
}
