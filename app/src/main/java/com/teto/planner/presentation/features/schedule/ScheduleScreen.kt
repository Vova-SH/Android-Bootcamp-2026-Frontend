package com.teto.planner.presentation.features.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.presentation.common.EditMeetingDialog
import com.teto.planner.presentation.common.MeetingDetailsDialog
import com.teto.planner.presentation.common.SharedCalendar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onCreateMeeting: () -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Мое расписание",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Обновить")
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Профиль")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is ScheduleUiState.Success) {
                FloatingActionButton(onClick = onCreateMeeting) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Создать встречу")
                }
            }
        }
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {
            when (val state = uiState) {
                is ScheduleUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ScheduleUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadMeetingsForMonth(LocalDate.now()) }
                    )
                }

                is ScheduleUiState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.refresh() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ScheduleContent(
                            state = state,
                            dateFormatter = dateFormatter,
                            onDateSelected = { viewModel.onDateSelected(it) },
                            onMonthChanged = { viewModel.loadMeetingsForMonth(it) },
                            onMeetingClick = { meeting -> viewModel.openMeetingDetails(meeting) }
                        )
                    }

                    state.selectedMeeting?.let { meeting ->
                        MeetingDetailsDialog(
                            meeting = meeting,
                            currentUserId = state.currentUserId,
                            isLoading = state.isMeetingDetailsLoading,
                            error = state.meetingDetailsError,
                            onDismiss = viewModel::closeMeetingDetails,
                            onEditClick = viewModel::onEditMeetingClick
                        )
                    }

                    state.meetingToEdit?.let { meeting ->
                        EditMeetingDialog(
                            meeting = meeting,
                            isLoading = state.isLoading,
                            onDismiss = viewModel::closeEditDialog,
                            onSave = { title, description ->
                                viewModel.updateMeeting(meeting.id, title, description)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleContent(
    state: ScheduleUiState.Success,
    dateFormatter: DateTimeFormatter,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (LocalDate) -> Unit,
    onMeetingClick: (Meeting) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            SharedCalendar(
                selectedDate = state.selectedDate,
                onDateSelected = onDateSelected,
                onMonthChanged = onMonthChanged,
                meetingsByDate = state.meetingsByDate
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Встречи на ${state.selectedDate.format(dateFormatter)}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (state.meetingsForSelectedDate.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.meetingsForSelectedDate) { meeting ->
                            TaskCard(
                                meeting = meeting,
                                onClick = { onMeetingClick(meeting) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(meeting: Meeting, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = meeting.startTime.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = meeting.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Участников: ${meeting.participants.size}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )

                meeting.room?.let {
                    Text(
                        text = it.name,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "На этот день встреч нет.\nОтличное время, чтобы отдохнуть!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Повторить запрос")
        }
    }
}