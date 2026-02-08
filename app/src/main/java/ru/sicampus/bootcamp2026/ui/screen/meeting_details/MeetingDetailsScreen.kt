    package ru.sicampus.bootcamp2026.ui.screen.meeting_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.data.dto.MemberDto
import ru.sicampus.bootcamp2026.data.source.SessionManager
import ru.sicampus.bootcamp2026.domain.model.Meeting
import ru.sicampus.bootcamp2026.ui.components.*
import ru.sicampus.bootcamp2026.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailsScreen(
    meetingId: Long,
    viewModel: MeetingDetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val navEvent by viewModel.navigationEvent.collectAsState()

    var showInviteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(meetingId) { viewModel.loadData(meetingId) }
    LaunchedEffect(navEvent) { if (navEvent) onBackClick() }

    val currentUserId = SessionManager.currentUserId

    JuicyBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, null, tint = TextPrimary)
                        }
                    },
                    actions = {
                        if (uiState is MeetingDetailsUiState.Content) {
                            val content = uiState as MeetingDetailsUiState.Content
                            if (content.meeting.creatorId == currentUserId) {
                                IconButton(onClick = { showEditDialog = true }) {
                                    Icon(Icons.Default.Edit, "Изменить", tint = BrandPrimary)
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            floatingActionButton = {
                if (uiState is MeetingDetailsUiState.Content) {
                    FloatingActionButton(
                        onClick = { showInviteDialog = true },
                        containerColor = BrandPrimary,
                        contentColor = Color.White
                    ) { Icon(Icons.Default.Add, null) }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (val state = uiState) {
                    is MeetingDetailsUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = BrandPrimary)
                    is MeetingDetailsUiState.Error -> Text(state.message, Modifier.align(Alignment.Center), color = BrandTertiary)
                    is MeetingDetailsUiState.Content -> {
                        MeetingContent(
                            meeting = state.meeting,
                            members = state.members,
                            currentUserId = currentUserId ?: -1L,
                            onDelete = { viewModel.deleteMeeting(meetingId) }
                        )

                        if (showInviteDialog) {
                            JuicyInviteDialog(
                                onDismiss = { showInviteDialog = false },
                                onSend = {
                                    viewModel.sendInvite(meetingId, it)
                                    showInviteDialog = false
                                }
                            )
                        }

                        if (showEditDialog) {
                            EditMeetingDialog(
                                meeting = state.meeting,
                                onDismiss = { showEditDialog = false },
                                onConfirm = { title, desc, place, date, duration ->
                                    viewModel.updateMeeting(meetingId, title, desc, place, date, duration)
                                    showEditDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MeetingContent(
    meeting: Meeting,
    members: List<MemberDto>,
    currentUserId: Long,
    onDelete: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    meeting.title,
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Организатор: ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        meeting.creatorName ?: "Неизвестно",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoBadge(Icons.Rounded.DateRange, meeting.startTime.take(10), Modifier.weight(1f))
                InfoBadge(Icons.Rounded.Info, "${meeting.durationMinutes} мин", Modifier.weight(1f))
            }
        }

        item {
            JuicyCard(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocationOn, null, tint = AccentPurple)
                    Spacer(Modifier.width(12.dp))
                    Text(meeting.location, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                }
            }
        }

        if (!meeting.description.isNullOrBlank()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Описание", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        meeting.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            }
        }

        item {
            Text("Участники (${members.size})", style = MaterialTheme.typography.headlineSmall)
        }

        items(members) { member ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(40.dp).background(SurfaceLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(member.member.firstName.take(1), fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("${member.member.firstName} ${member.member.secondName}", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text(member.status, style = MaterialTheme.typography.labelSmall, color = BrandPrimary)
                }
            }
        }

        if (meeting.creatorId == currentUserId) {
            item {
                Spacer(Modifier.height(24.dp))
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandTertiary),
                    border = null
                ) {
                    Icon(Icons.Default.Delete, null, tint = BrandTertiary)
                    Spacer(Modifier.width(8.dp))
                    Text("Удалить встречу", color = BrandTertiary)
                }
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun EditMeetingDialog(
    meeting: Meeting,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf(meeting.title) }
    var description by remember { mutableStateOf(meeting.description ?: "") }
    var place by remember { mutableStateOf(meeting.location) }
    var date by remember { mutableStateOf(meeting.startTime) }
    var duration by remember { mutableStateOf(meeting.durationMinutes.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактирование встречи") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                JuicyTextField(value = title, onValueChange = { title = it }, label = "Название")
                JuicyTextField(value = description, onValueChange = { description = it }, label = "Описание", singleLine = false)
                JuicyTextField(value = place, onValueChange = { place = it }, label = "Место")
                JuicyTextField(value = date, onValueChange = { date = it }, label = "Дата (ISO 8601)")
                JuicyTextField(value = duration, onValueChange = { duration = it }, label = "Длительность (мин)")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dur = duration.toIntOrNull() ?: 60
                    onConfirm(title, description, place, date, dur)
                },
                enabled = title.isNotBlank() && place.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        containerColor = SurfaceWhite
    )
}

@Composable
fun InfoBadge(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = SurfaceLight,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, Modifier.size(20.dp), tint = TextSecondary)
            Spacer(Modifier.width(8.dp))
            Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        }
    }
}

@Composable
fun JuicyInviteDialog(onDismiss: () -> Unit, onSend: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Пригласить участника") },
        text = {
            JuicyTextField(value = email, onValueChange = { email = it }, label = "Email")
        },
        confirmButton = {
            Button(
                onClick = { onSend(email) },
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) { Text("Отправить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        containerColor = SurfaceWhite
    )
}