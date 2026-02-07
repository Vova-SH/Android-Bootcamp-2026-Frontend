package com.teto.planner.presentation.features.meeting_create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.model.meeting.IntersectionResponse
import com.teto.planner.domain.model.meeting.IntersectionSlot
import com.teto.planner.domain.model.meeting.IntersectionSlotStatus
import com.teto.planner.domain.model.user.UserSummary
import com.teto.planner.presentation.theme.AppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

// fixme этот ужас надо чинить, вот список багов:
// можно выбрать себя из списка

// todo
// добавить кнопку экстренной брони - взял юзеров - тык - создал встречу на ближайшее время
// посмотреть как работают пересечения - в очень редких кейсах они появляются
// в случаях конфликтов сделать кликабельный профиль - тык - alert dialog с инфой по челу и большая кнопка с телегой
// кэшировать список недавно выбранных пользователей - в datastore подсказки хранить
// подумать насчет блока чипсов которые не подходят к местному времени

@Composable
fun MeetingCreateScreen(
    viewModel: MeetingCreateViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    MeetingCreateContent(
        state = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onLoadMoreUsers = viewModel::onLoadMoreUsers,
        onParticipantSelected = viewModel::onParticipantSelected,
        onParticipantRemoved = viewModel::onParticipantRemoved,
        onDateSelected = viewModel::onDateSelected,
        onHourSelected = viewModel::onHourSelected,
        onRoomSelected = viewModel::onRoomSelected,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onCreateMeeting = { viewModel.onCreateMeeting(onSuccess) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MeetingCreateContent(
    state: MeetingCreateUiState,
    onSearchQueryChanged: (String) -> Unit,
    onLoadMoreUsers: () -> Unit,
    onParticipantSelected: (UserSummary) -> Unit,
    onParticipantRemoved: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onHourSelected: (Int) -> Unit,
    onRoomSelected: (String) -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCreateMeeting: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Создать встречу") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            when (state) {
                is MeetingCreateUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is MeetingCreateUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }

                is MeetingCreateUiState.Success -> {
                    SuccessLayout(
                        state = state,
                        onSearchQueryChanged = onSearchQueryChanged,
                        onLoadMoreUsers = onLoadMoreUsers,
                        onParticipantSelected = onParticipantSelected,
                        onParticipantRemoved = onParticipantRemoved,
                        onDateSelected = onDateSelected,
                        onHourSelected = onHourSelected,
                        onRoomSelected = onRoomSelected,
                        onTitleChanged = onTitleChanged,
                        onDescriptionChanged = onDescriptionChanged,
                        onCreateMeeting = onCreateMeeting
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun SuccessLayout(
    state: MeetingCreateUiState.Success,
    onSearchQueryChanged: (String) -> Unit,
    onLoadMoreUsers: () -> Unit,
    onParticipantSelected: (UserSummary) -> Unit,
    onParticipantRemoved: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onHourSelected: (Int) -> Unit,
    onRoomSelected: (String) -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCreateMeeting: () -> Unit
) {
    var searchExpanded by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.selectedDate
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val today =
                        LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                    return utcTimeMillis >= today
                }
            }
        )

        val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            onDateSelected(selectedDate)
                        }
                        showDatePicker = false
                    },
                    enabled = confirmEnabled
                ) {
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (searchExpanded) 0.dp else 16.dp)
                .padding(top = if (searchExpanded) 0.dp else 16.dp, bottom = 8.dp)
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = state.searchQuery,
                onQueryChange = onSearchQueryChanged,
                onSearch = { searchExpanded = false },
                active = searchExpanded,
                onActiveChange = { searchExpanded = it },
                placeholder = { Text("Добавить участников") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                        alpha = 0.5f
                    )
                )
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.searchResults) { user ->
                        val isSelected = state.selectedParticipants.any { it.id == user.id }

                        ListItem(
                            headlineContent = { Text(user.name) },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Уже выбран",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            modifier = Modifier
                                .clickable(enabled = !isSelected) {
                                    onParticipantSelected(user)
                                    searchExpanded = false
                                    onSearchQueryChanged("")
                                }
                                .alpha(if (isSelected) 0.5f else 1f)
                        )
                    }

                    if (state.canLoadMoreUsers) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                LaunchedEffect(Unit) {
                                    onLoadMoreUsers()
                                }
                            }
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.selectedParticipants) { user ->
                ParticipantCard(
                    user = user,
                    onRemove = { onParticipantRemoved(user.id) }
                )
            }

            if (state.selectedParticipants.isNotEmpty()) {
                item { HorizontalDivider() }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Когда?",
                            style = MaterialTheme.typography.titleLarge
                        )

                        OutlinedButton(
                            onClick = { showDatePicker = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditCalendar,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val formatter = DateTimeFormatter.ofPattern(
                                "d MMM yyyy",
                                Locale.forLanguageTag("ru")
                            )
                            Text(text = state.selectedDate.format(formatter))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.intersectionResponse != null) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.intersectionResponse.slots.forEach { slot ->
                                TimeSlotChip(
                                    slot = slot,
                                    isSelected = state.selectedHour == slot.hour,
                                    onClick = { onHourSelected(slot.hour) }
                                )
                            }
                        }
                    }
                }

                if (state.selectedHour != null) {
                    item { HorizontalDivider() }

                    item {
                        Text(
                            text = "Где?",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (state.isLoadingRooms) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                state.availableRooms.forEach { room ->
                                    RoomChip(
                                        room = room,
                                        isSelected = state.selectedRoomId == room.id,
                                        onClick = { onRoomSelected(room.id) }
                                    )
                                }
                            }

                            if (!state.isCapacityValid && state.selectedRoom != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "В выбранной комнате недостаточно мест (${state.selectedRoom!!.capacity}) для ${state.selectedParticipants.size + 1} участников (включая вас).",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    item { HorizontalDivider() }

                    item {
                        OutlinedTextField(
                            value = state.title,
                            onValueChange = onTitleChanged,
                            label = { Text("Тема встречи") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = state.description,
                            onValueChange = onDescriptionChanged,
                            label = { Text("Описание (опционально)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            maxLines = 3
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onCreateMeeting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = state.canSubmit,
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            if (state.isSubmitting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Создать встречу")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParticipantCard(user: UserSummary, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(width = 2.dp, color = Color.Green, shape = CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = user.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onRemove) {
                Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null)
            }
        }
    }
}

@Composable
fun TimeSlotChip(slot: IntersectionSlot, isSelected: Boolean, onClick: () -> Unit) {
    val conflictUser = slot.conflictedUsers.firstOrNull()
    val isBusy = slot.status == IntersectionSlotStatus.DISABLED

    Box(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .alpha(if (isBusy && !isSelected) 0.5f else 1f)
            .clickable(enabled = !isBusy) { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(text = slot.label ?: "", style = MaterialTheme.typography.bodySmall)
            }
            if (conflictUser != null) {
                Text(
                    text = "Конфликт: ${conflictUser.name}",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun RoomChip(room: Room, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = "${room.name} (${room.capacity} чел.)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

class MeetingCreateStateProvider : PreviewParameterProvider<MeetingCreateUiState> {
    private val mockUser = UserSummary(id = "1", name = "Иван Иванов", avatarUrl = null)
    private val mockRoom1 = Room(id = "1", name = "Переговорная 1", capacity = 10)
    private val mockRoom2 = Room(id = "2", name = "Большой зал", capacity = 20)
    private val mockResponse = IntersectionResponse(
        date = LocalDate.now(),
        organizer = mockUser,
        users = listOf(mockUser),
        slots = List(6) { i ->
            IntersectionSlot(
                hour = i + 9,
                status = if (i == 2) IntersectionSlotStatus.DISABLED else IntersectionSlotStatus.GREEN,
                conflictedUsers = if (i == 2) listOf(mockUser) else emptyList(),
                label = "${i + 9}:00"
            )
        }
    )

    override val values = sequenceOf(
        MeetingCreateUiState.Loading,
        MeetingCreateUiState.Success(
            selectedParticipants = listOf(mockUser),
            intersectionResponse = mockResponse,
            selectedHour = 10,
            availableRooms = listOf(mockRoom1, mockRoom2),
            title = "Обсуждение проекта",
            description = "Краткое описание встречи"
        ),
        MeetingCreateUiState.Error("Ошибка сети")
    )
}

@Preview(showBackground = true)
@Composable
fun MeetingCreatePreview(@PreviewParameter(MeetingCreateStateProvider::class) state: MeetingCreateUiState) {
    AppTheme {
        MeetingCreateContent(
            state = state,
            onSearchQueryChanged = {},
            onLoadMoreUsers = {},
            onParticipantSelected = {},
            onParticipantRemoved = {},
            onDateSelected = {},
            onHourSelected = {},
            onRoomSelected = {},
            onTitleChanged = {},
            onDescriptionChanged = {},
            onCreateMeeting = {},
            onBack = {}
        )
    }
}