package com.example.create_meet.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.comon.ErrorState
import com.example.create_meet.data.dto.MeetingResponse
import com.example.create_meet.data.dto.MeetingStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.example.create_meet.data.dto.InvitationStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsListScreen(
    viewModel: EventsListScreenViewModel,
    modifier: Modifier = Modifier,
    onAddNewMeet: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullState = rememberPullToRefreshState()
    val actionState by viewModel.actionState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadInvitations()
    }

    LaunchedEffect(actionState) {
        when (actionState) {
            is ActionState.Success -> {
                snackbarHostState.showSnackbar("Успешно")
                viewModel.resetActionState()
            }
            is ActionState.Error -> {
                snackbarHostState.showSnackbar((actionState as ActionState.Error).message)
                viewModel.resetActionState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Расписание встреч") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewMeet) {
                Icon(Icons.Default.Add, contentDescription = "Add meet")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = viewModel::refresh,
            state = pullState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {

                when (uiState) {
                    is EventsUiState.Loading -> {
                        item {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is EventsUiState.Error -> {
                        item {
                            ErrorState(
                                onRefresh = viewModel::refresh,
                                message = (uiState as EventsUiState.Error).message
                            )
                        }
                    }

                    is EventsUiState.Success -> {
                        val meetings = (uiState as EventsUiState.Success).meetings
                        if (meetings.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Нет запланированных встреч")
                                }
                            }
                        } else {
                            items(items = meetings, key = { it.id }) { meeting ->
                                MeetingItem(
                                    meeting = meeting,
                                    onAccept = { viewModel.accept( meeting.id) },
                                    onDecline = { viewModel.decline(meeting.id) }
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun MeetingItem(
    meeting: MeetingResponse,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = meeting.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatMeetingTime(meeting.startTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (!meeting.description.isNullOrBlank()) {
                Text(
                    text = meeting.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = "📍 ${meeting.location}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = when (meeting.status) {
                    MeetingStatus.SCHEDULED -> "Назначена"
                    MeetingStatus.CANCELLED -> "Отменена"
                    MeetingStatus.COMPLETED -> "Завершена"
                },
                color = when (meeting.status) {
                    MeetingStatus.SCHEDULED -> MaterialTheme.colorScheme.primary
                    MeetingStatus.CANCELLED -> MaterialTheme.colorScheme.error
                    MeetingStatus.COMPLETED -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            if (meeting.invitations.lastOrNull()?.status == InvitationStatus.PENDING) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onAccept(meeting.invitations.find { it.inviteeId == meeting.organizerId }!!.inviteeId) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Принять")
                    }
                    OutlinedButton(
                        onClick = { onDecline(meeting.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Отклонить")
                    }
                }
            }
        }
    }
}

private fun formatMeetingTime(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm").format(localDateTime)
    } catch (e: Exception) {
        Log.e("EventsListScreen", "Failed to parse time: $isoString", e)
        isoString
    }
}
