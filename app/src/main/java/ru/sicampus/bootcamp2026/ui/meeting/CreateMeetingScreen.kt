package ru.sicampus.bootcamp2026.ui.meeting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.sicampus.bootcamp2026.ui.theme.LightGreen

/**
 * Экран создания встречи
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetingScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateMeetingViewModel = hiltViewModel<CreateMeetingViewModel>()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Навигация назад при успешном создании
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (state.currentStep == CreateMeetingStep.BASIC_INFO) {
                    onNavigateBack()
                } else {
                    viewModel.onEvent(CreateMeetingUiEvent.PreviousStep)
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Create Meeting",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = when (state.currentStep) {
                        CreateMeetingStep.BASIC_INFO -> "Step 1 of 3: Basic Info"
                        CreateMeetingStep.SELECT_PARTICIPANTS -> "Step 2 of 3: Select Participants"
                        CreateMeetingStep.SELECT_TIME -> "Step 3 of 3: Select Time"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Progress indicator
        LinearProgressIndicator(
            progress = {
                when (state.currentStep) {
                    CreateMeetingStep.BASIC_INFO -> 0.33f
                    CreateMeetingStep.SELECT_PARTICIPANTS -> 0.66f
                    CreateMeetingStep.SELECT_TIME -> 1f
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = LightGreen,
            trackColor = Color.White.copy(alpha = 0.2f)
        )

        // Content based on current step
        when (state.currentStep) {
            CreateMeetingStep.BASIC_INFO -> {
                BasicInfoStep(state, viewModel)
            }
            CreateMeetingStep.SELECT_PARTICIPANTS -> {
                SelectParticipantsStep(state, viewModel)
            }
            CreateMeetingStep.SELECT_TIME -> {
                SelectTimeStep(state, viewModel)
            }
        }
    }

    // Error dialog
    if (state.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CreateMeetingUiEvent.DismissError) },
            title = { Text("Error") },
            text = { Text(state.error!!) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(CreateMeetingUiEvent.DismissError) }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun BasicInfoStep(
    state: CreateMeetingUiState,
    viewModel: CreateMeetingViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            // Title field
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onEvent(CreateMeetingUiEvent.TitleChanged(it)) },
                label = { Text("Title *", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = LightGreen,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = LightGreen,
                    errorBorderColor = Color.Red,
                    errorTextColor = Color.White
                ),
                isError = state.titleError != null,
                supportingText = state.titleError?.let { { Text(it, color = Color.Red) } },
                singleLine = true
            )

            // Description field
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(CreateMeetingUiEvent.DescriptionChanged(it)) },
                label = { Text("Description (optional)", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = LightGreen,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = LightGreen
                ),
                maxLines = 4
            )

            // Location field
            OutlinedTextField(
                value = state.location,
                onValueChange = { viewModel.onEvent(CreateMeetingUiEvent.LocationChanged(it)) },
                label = { Text("Location (optional)", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = LightGreen,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = LightGreen
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            // Next button
            Button(
                onClick = { viewModel.onEvent(CreateMeetingUiEvent.NextStep) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Next: Select Participants",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

@Composable
private fun SelectParticipantsStep(
    state: CreateMeetingUiState,
    viewModel: CreateMeetingViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Select meeting participants",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = "${state.selectedParticipants.size} selected",
            style = MaterialTheme.typography.bodyMedium,
            color = LightGreen
        )

        if (state.participantsError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.participantsError!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Participants list
        if (state.availableParticipants.isEmpty()) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LightGreen)
                }
            } else {
                Text(
                    text = "No users available",
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.availableParticipants.size) { index ->
                    val participant = state.availableParticipants[index]
                    ParticipantCheckboxItem(
                        participant = participant,
                        onToggle = {
                            viewModel.onEvent(
                                CreateMeetingUiEvent.ParticipantToggled(participant.id)
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next button
        Button(
            onClick = { viewModel.onEvent(CreateMeetingUiEvent.NextStep) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = state.selectedParticipants.isNotEmpty()
        ) {
            Text(
                text = "Next: Select Time",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SelectTimeStep(
    state: CreateMeetingUiState,
    viewModel: CreateMeetingViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Select available time slot",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date picker
        DatePickerField(
            selectedDate = state.selectedDate,
            onDateSelected = { viewModel.onEvent(CreateMeetingUiEvent.DateChanged(it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.timeSlotError != null) {
            Text(
                text = state.timeSlotError!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Available time slots
        if (state.isLoadingFreeTime) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = LightGreen)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading available time slots...",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        } else if (state.freeTimeSlots.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No available time slots for selected date",
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = {
                            viewModel.onEvent(CreateMeetingUiEvent.DateChanged(state.selectedDate.plusDays(1)))
                        }
                    ) {
                        Text("Try next day", color = LightGreen)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.freeTimeSlots.size) { index ->
                    val slot = state.freeTimeSlots[index]
                    TimeSlotCard(
                        slot = slot,
                        isSelected = state.selectedTimeSlot == slot,
                        onClick = {
                            viewModel.onEvent(CreateMeetingUiEvent.TimeSlotSelected(slot))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create button
        Button(
            onClick = { viewModel.onEvent(CreateMeetingUiEvent.CreateMeeting) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = !state.isLoading && state.selectedTimeSlot != null
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Black
                )
            } else {
                Text(
                    text = "Create Meeting",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TimeSlotCard(
    slot: TimeSlotItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LightGreen.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, LightGreen)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = if (isSelected) LightGreen else Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = slot.displayTime,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = LightGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun ParticipantCheckboxItem(
    participant: ParticipantItem,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (participant.isSelected) {
                LightGreen.copy(alpha = 0.2f)
            } else {
                Color.White.copy(alpha = 0.05f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = participant.isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = LightGreen,
                    uncheckedColor = Color.White.copy(alpha = 0.6f),
                    checkmarkColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = participant.username,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (participant.isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
        onValueChange = {},
        label = { Text("Date *", color = Color.White.copy(alpha = 0.7f)) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = LightGreen,
            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            disabledTextColor = Color.White,
            disabledBorderColor = Color.White.copy(alpha = 0.5f)
        ),
        enabled = false,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select date",
                tint = LightGreen
            )
        }
    )

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toEpochDay() * 24 * 60 * 60 * 1000
        )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val epochDay = millis / (24 * 60 * 60 * 1000)
                            onDateSelected(LocalDate.ofEpochDay(epochDay))
                        }
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

