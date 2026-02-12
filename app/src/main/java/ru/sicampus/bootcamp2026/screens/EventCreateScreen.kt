package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.components.*
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.MeetingTypeDto
import ru.sicampus.bootcamp2026.data.repo.BootcampRepository
import ru.sicampus.bootcamp2026.data.remote.AuthStore
import ru.sicampus.bootcamp2026.data.util.toUiMessage
import ru.sicampus.bootcamp2026.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreateScreen(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf<EventUiType?>(null) }
    var dateText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var onlineLink by remember { mutableStateOf("") }
    var inviteInput by remember { mutableStateOf("") }
    val invitedEmails = remember { mutableStateListOf<String>() }
    var description by remember { mutableStateOf("") }

    val repo = remember { BootcampRepository() }
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val today = LocalDate.now()

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val me by AuthStore.user.collectAsState()
    var userLogins by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Подсказки для приглашений берём с backend.
        runCatching { repo.getAllUsers() }
            .onSuccess { users ->
                userLogins = users.mapNotNull { it.login }.distinct().sorted()
            }
            .onFailure {
                userLogins = emptyList()
            }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val local = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return !local.isBefore(today)
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val local = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        selectedDate = local
                        dateText = "%02d.%02d.%04d".format(
                            local.dayOfMonth,
                            local.monthValue,
                            local.year
                        )
                    }
                    showDatePicker = false
                }) {
                    Text("ОК")
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

    val hours = remember { (9..21).map { String.format("%02d:00", it) } }

    if (showStartPicker) {
        TimePickerDialog(
            title = "Время начала",
            hours = hours,
            onDismiss = { showStartPicker = false },
            onTimeSelected = { selected ->
                startTime = selected
                endTime = nextHourOrEmpty(selected)
            }
        )
    }

    if (showEndPicker) {
        val endHours = nextHourOrEmpty(startTime).let { if (it.isBlank()) emptyList() else listOf(it) }

        TimePickerDialog(
            title = "Время окончания",
            hours = endHours,
            onDismiss = { showEndPicker = false },
            onTimeSelected = { selected ->
                endTime = selected
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(136.dp)) }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(56.dp))
                        .background(Gray)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        EventTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = "Название мероприятия"
                        )

                        EventTypeSelector(
                            value = type,
                            onValueChange = { type = it }
                        )

                        EventDateField(
                            value = dateText,
                            onValueChange = { dateText = it },
                            onIconClick = { showDatePicker = true }
                        )

                        EventTimeRow(
                            startTime = startTime,
                            endTime = endTime,
                            onStartClick = { showStartPicker = true },
                            onEndClick = { if (startTime.isNotBlank()) showEndPicker = true }
                        )

                        val placeEnabled =
                            type == EventUiType.Offline || type == EventUiType.Hybrid
                        EventTextField(
                            value = place,
                            onValueChange = { place = it },
                            placeholder = "Место встречи",
                            enabled = placeEnabled
                        )

                        val linkEnabled =
                            type == EventUiType.Hybrid || type == EventUiType.Online
                        EventTextField(
                            value = onlineLink,
                            onValueChange = { onlineLink = it },
                            placeholder = "Онлайн ссылка",
                            enabled = linkEnabled
                        )

                        if (invitedEmails.isNotEmpty()) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                invitedEmails.forEach { email ->
                                    AssistChip(
                                        onClick = { },
                                        label = { Text(email) },
                                        trailingIcon = {
                                            IconButton(onClick = { invitedEmails.remove(email) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Удалить"
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }


                        InviteEmailField(
                            value = inviteInput,
                            onValueChange = { inviteInput = it },
                            suggestions = userLogins.filterNot { it.equals(me?.login, ignoreCase = true) },
                            onSuggestionClick = { email ->
                                if (email.equals(me?.login, ignoreCase = true)) {
                                    errorText = "Нельзя приглашать самого себя"
                                } else if (email.isNotBlank() && email !in invitedEmails) {
                                    invitedEmails.add(email)
                                }
                                inviteInput = ""
                            }
                        )


                        EventDescriptionField(
                            value = description,
                            onValueChange = { description = it }
                        )

                        Spacer(Modifier.height(8.dp))

                        PrimaryButton(
                            text = "Создать",
                            enabled = !isSaving,
                            onClick = {
                                errorText = null
                                val dateIso = selectedDate?.toString()
                                if (title.isBlank() || dateIso.isNullOrBlank() || startTime.isBlank() || endTime.isBlank() || type == null) {
                                    errorText = "Заполни: название, тип, дату, время начала и окончания"
                                    return@PrimaryButton
                                }

                                val meetingType = when (type) {
                                    EventUiType.Online -> MeetingTypeDto.ONLINE
                                    EventUiType.Offline -> MeetingTypeDto.OFFLINE
                                    EventUiType.Hybrid -> MeetingTypeDto.HYBRID
                                    else -> MeetingTypeDto.OFFLINE
                                }

                                val startAtIso = "${dateIso}T${startTime}:00"
                                val endAtIso = "${dateIso}T${endTime}:00"

                                val invitees = invitedEmails
                                    .filterNot { it.equals(me?.login, ignoreCase = true) }

                                val dto = MeetingDto(
                                    title = title,
                                    startAt = startAtIso,
                                    endAt = endAtIso,
                                    description = description.ifBlank { null },
                                    type = meetingType,
                                    location = place.ifBlank { null },
                                    url = onlineLink.ifBlank { null },
                                    inviteeLogins = invitees
                                )

                                isSaving = true
                                scope.launch {
                                    runCatching { repo.createMeeting(dto) }
                                        .onSuccess {
                                            title = ""
                                            type = null
                                            dateText = ""
                                            selectedDate = null
                                            startTime = ""
                                            endTime = ""
                                            place = ""
                                            onlineLink = ""
                                            invitedEmails.clear()
                                            inviteInput = ""
                                            description = ""
                                            onCreateClick()
                                        }
                                        .onFailure {
                                            errorText = it.toUiMessage()
                                        }
                                    isSaving = false
                                }
                            }
                        )

                        if (!errorText.isNullOrBlank()) {
                            Text(
                                text = errorText!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(96.dp)) }
        }

        MainTopBar(
            title = "Создание мероприятия",
            modifier = Modifier.align(Alignment.TopCenter)
        )

        MainBottomBar(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private fun nextHourOrEmpty(start: String): String {
    val h = start.substringBefore(":").toIntOrNull() ?: return ""
    val next = h + 1
    return if (next in 0..23) String.format("%02d:00", next) else ""
}


@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun EventCreateScreenPreview() {
    AppTheme {
        EventCreateScreen(
            currentTab = MainTab.Add,
            onTabSelected = {}
        )
    }
}
