package com.example.meet.ui.screens.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet.R
import kotlinx.coroutines.launch
import com.example.meet.data.dto.MeetingDto
import com.example.meet.data.dto.InvitationDto
import com.example.meet.data.dto.InvitationResponseStatus
import com.example.meet.data.source.DataLocator
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

sealed class InvitationsListUiState {
    data object Loading : InvitationsListUiState()
    data class Success(
        val pending: List<InvitationWithMeeting>,
        val accepted: List<InvitationWithMeeting>,
        val declined: List<InvitationWithMeeting>
    ) : InvitationsListUiState()
    data class Error(val message: String) : InvitationsListUiState()
}

data class InvitationWithMeeting(
    val invitation: InvitationDto,
    val meeting: MeetingDto?
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun InvitationsListScreen(navController: NavHostController) {
    val ds = remember { DataLocator.userInfoDataSource }
    val invDs = remember { DataLocator.invitationDataSource }
    val scope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf<InvitationsListUiState>(InvitationsListUiState.Loading) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val user = ds.loadCurrentUser()
                val invitations = invDs.getInvitations(user.id.toInt()).getOrThrow()
                val allMeetings = ds.loadAllMeetings()
                val meetingMap = allMeetings.associateBy { it.id }
                val mapped = invitations.map { inv ->
                    InvitationWithMeeting(invitation = inv, meeting = meetingMap[inv.meetingId])
                }
                val pending = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.PENDING, true) }
                val accepted = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.ACCEPTED, true) }
                val declined = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.DECLINED, true) }
                uiState = InvitationsListUiState.Success(
                    pending = pending,
                    accepted = accepted,
                    declined = declined
                )
            } catch (e: Exception) {
                uiState = InvitationsListUiState.Error("Не удалось загрузить приглашения: ${e.message}")
            }
        }
    }

    val primaryDarkBlue = colorScheme.primary
    val onPrimary = colorScheme.onPrimary
    val surfaceColor = colorScheme.background
    val errorColor = colorScheme.error
    val errorContainer = colorScheme.errorContainer
    val onSurfaceVariant = colorScheme.onSurfaceVariant

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Приглашения",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("main_meet") {
                                    popUpTo("main_meet") { inclusive = true }
                                }
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = onPrimary
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Назад",
                            modifier = Modifier.size(24.dp),
                            tint = onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = primaryDarkBlue,
                    titleContentColor = onPrimary,
                    navigationIconContentColor = onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(surfaceColor)
        ) {
            when (val state = uiState) {
                InvitationsListUiState.Loading -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = primaryDarkBlue
                    )
                    Text(
                        text = "Загрузка приглашений...",
                        color = onSurfaceVariant
                    )
                }

                is InvitationsListUiState.Error -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Ошибка",
                            modifier = Modifier.size(40.dp),
                            tint = errorColor
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Ошибка загрузки",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = errorColor
                        )
                        Text(
                            text = state.message,
                            color = onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val user = ds.loadCurrentUser()
                                    val invitations = invDs.getInvitations(user.id.toInt()).getOrThrow()
                                    val allMeetings = ds.loadAllMeetings()
                                    val meetingMap = allMeetings.associateBy { it.id }
                                    val mapped = invitations.map { inv ->
                                        InvitationWithMeeting(invitation = inv, meeting = meetingMap[inv.meetingId])
                                    }
                                    val pending = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.PENDING, true) }
                                    val accepted = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.ACCEPTED, true) }
                                    val declined = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.DECLINED, true) }
                                    uiState = InvitationsListUiState.Success(
                                        pending = pending,
                                        accepted = accepted,
                                        declined = declined
                                    )
                                } catch (e: Exception) {
                                    uiState = InvitationsListUiState.Error("Не удалось загрузить приглашения: ${e.message}")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = errorContainer,
                            contentColor = errorColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Повторить")
                    }
                }

                is InvitationsListUiState.Success -> {
                    InvitationsGroupedContent(
                        pending = state.pending,
                        accepted = state.accepted,
                        declined = state.declined,
                        navController = navController,
                        primaryDarkBlue = primaryDarkBlue,
                        onAccept = { item ->
                            scope.launch {
                                try {
                                    invDs.updateInvitationResponse(item.invitation.id.toInt(), InvitationResponseStatus.ACCEPTED)
                                    val user = ds.loadCurrentUser()
                                    val invitations = invDs.getInvitations(user.id.toInt()).getOrThrow()
                                    val allMeetings = ds.loadAllMeetings()
                                    val meetingMap = allMeetings.associateBy { it.id }
                                    val mapped = invitations.map { inv ->
                                        InvitationWithMeeting(invitation = inv, meeting = meetingMap[inv.meetingId])
                                    }
                                    val pending = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.PENDING, true) }
                                    val accepted = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.ACCEPTED, true) }
                                    val declined = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.DECLINED, true) }
                                    uiState = InvitationsListUiState.Success(pending, accepted, declined)
                                } catch (e: Exception) {
                                    uiState = InvitationsListUiState.Error("Не удалось принять приглашение: ${e.message}")
                                }
                            }
                        },
                        onDecline = { item, comment ->
                            scope.launch {
                                try {
                                    invDs.updateInvitationResponse(
                                        invitationId = item.invitation.id.toInt(),
                                        responseStatus = InvitationResponseStatus.DECLINED,
                                        comment = comment
                                    )
                                    val user = ds.loadCurrentUser()
                                    val invitations = invDs.getInvitations(user.id.toInt()).getOrThrow()
                                    val allMeetings = ds.loadAllMeetings()
                                    val meetingMap = allMeetings.associateBy { it.id }
                                    val mapped = invitations.map { inv ->
                                        InvitationWithMeeting(invitation = inv, meeting = meetingMap[inv.meetingId])
                                    }
                                    val pending = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.PENDING, true) }
                                    val accepted = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.ACCEPTED, true) }
                                    val declined = mapped.filter { it.invitation.responseStatus.equals(InvitationResponseStatus.DECLINED, true) }
                                    uiState = InvitationsListUiState.Success(pending, accepted, declined)
                                } catch (e: Exception) {
                                    uiState = InvitationsListUiState.Error("Не удалось отклонить приглашение: ${e.message}")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun InvitationsGroupedContent(
    pending: List<InvitationWithMeeting>,
    accepted: List<InvitationWithMeeting>,
    declined: List<InvitationWithMeeting>,
    navController: NavHostController,
    primaryDarkBlue: Color,
    onAccept: (InvitationWithMeeting) -> Unit,
    onDecline: (InvitationWithMeeting, String?) -> Unit
) {
    val totalCount = pending.size + accepted.size + declined.size
    val allActiveCount = pending.size
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
    ) {
        item {
            InvitationsHeaderSection(
                meetingsCount = totalCount,
                activeMeetingsCount = allActiveCount,
                primaryDarkBlue = primaryDarkBlue
            )
        }
        if (totalCount == 0) {
            item {
                EmptyInvitationsSection(
                    primaryDarkBlue = primaryDarkBlue
                )
            }
        } else {
            item {
                SectionHeader(title = "Ожидание", primaryDarkBlue = primaryDarkBlue, count = pending.size)
            }
            items(pending) { item ->
                InvitationItem(
                    item = item,
                    primaryDarkBlue = primaryDarkBlue,
                    onAccept = { onAccept(item) },
                    onDecline = { i, comment -> onDecline(i, comment) },
                    onClick = { navController.navigate("invitation_details/${item.invitation.id}") }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                SectionHeader(title = "Принятые", primaryDarkBlue = primaryDarkBlue, count = accepted.size)
            }
            items(accepted) { item ->
                InvitationItem(
                    item = item,
                    primaryDarkBlue = primaryDarkBlue,
                    onAccept = {},
                    onDecline = { _, _ -> },
                    onClick = { navController.navigate("invitation_details/${item.invitation.id}") }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                SectionHeader(title = "Отклоненные", primaryDarkBlue = primaryDarkBlue, count = declined.size)
            }
            items(declined) { item ->
                InvitationItem(
                    item = item,
                    primaryDarkBlue = primaryDarkBlue,
                    onAccept = {},
                    onDecline = { _, _ -> },
                    onClick = { navController.navigate("invitation_details/${item.invitation.id}") }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun InvitationsHeaderSection(
    meetingsCount: Int,
    activeMeetingsCount: Int,
    primaryDarkBlue: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Мои приглашения",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = primaryDarkBlue
                )
                Text(
                    text = "Всего активных: $activeMeetingsCount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = meetingsCount.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = primaryDarkBlue
                )
            }
        }
    }
}

@Composable
private fun EmptyInvitationsSection(
    primaryDarkBlue: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person_add),
                    contentDescription = "Нет приглашений",
                    modifier = Modifier.size(36.dp),
                    tint = primaryDarkBlue
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Нет активных приглашений",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryDarkBlue
                )
                Text(
                    text = "Вас пока не пригласили на встречи",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun InvitationItem(
    item: InvitationWithMeeting,
    primaryDarkBlue: Color,
    onAccept: (InvitationWithMeeting) -> Unit,
    onDecline: (InvitationWithMeeting, String?) -> Unit,
    onClick: () -> Unit
) {
    var showDeclineDialog by remember { mutableStateOf(false) }
    var declineComment by remember { mutableStateOf("") }
    val meeting = item.meeting
    val statusColor = when (item.invitation.responseStatus.uppercase()) {
        InvitationResponseStatus.PENDING -> colorScheme.primary
        InvitationResponseStatus.ACCEPTED -> colorScheme.tertiary
        InvitationResponseStatus.DECLINED -> colorScheme.error
        else -> colorScheme.secondary
    }

    val priorityColor = when (meeting?.meetingPriority) {
        "HIGH" -> colorScheme.error
        "MEDIUM" -> colorScheme.primary
        "LOW" -> colorScheme.secondary
        else -> colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(statusColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = meeting?.title ?: "Встреча #${item.invitation.meetingId}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryDarkBlue
                        )

                        if (meeting?.description?.isNotBlank() == true) {
                            Text(
                                text = meeting.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(statusColor.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = when (item.invitation.responseStatus.uppercase()) {
                                InvitationResponseStatus.PENDING -> "Ожидание"
                                InvitationResponseStatus.ACCEPTED -> "Принято"
                                InvitationResponseStatus.DECLINED -> "Отклонено"
                                else -> item.invitation.responseStatus
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = "Время",
                            modifier = Modifier.size(16.dp),
                            tint = primaryDarkBlue
                        )
                        Text(
                            text = formatMeetingTime(meeting),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(priorityColor.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = when (meeting?.meetingPriority) {
                                    "HIGH" -> "Высокий приоритет"
                                    "MEDIUM" -> "Средний приоритет"
                                    "LOW" -> "Низкий приоритет"
                                    else -> meeting?.meetingPriority ?: "Неизвестно"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = priorityColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val showActions = item.invitation.responseStatus.equals(InvitationResponseStatus.PENDING, true)
                    if (showActions) {
                        Button(
                            onClick = { onAccept(item) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryDarkBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Принять")
                        }
                        OutlinedButton(
                            onClick = {
                                if (meeting?.meetingPriority == "HIGH") {
                                    showDeclineDialog = true
                                } else {
                                    onDecline(item, null)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = primaryDarkBlue
                            ),
                            border = BorderStroke(1.dp, primaryDarkBlue),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Отклонить")
                        }
                    }
                }
            }
        }
    }

    if (showDeclineDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    onDecline(item, declineComment.ifBlank { null })
                }) {
                    Text("Отклонить")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                }) {
                    Text("Отмена")
                }
            },
            title = { Text("Причина отклонения приглашения") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Напишите причину отклонения приглашения (необязательно)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = declineComment,
                        onValueChange = { declineComment = it },
                        label = { Text("Описание") },
                        placeholder = { Text("Напишите причину отклонения приглашения") },
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun SectionHeader(title: String, primaryDarkBlue: Color, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = primaryDarkBlue
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(colorScheme.background)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = primaryDarkBlue
            )
        }
    }
}

private fun formatMeetingTime(meeting: MeetingDto?): String {
    fun parseTime(value: String): String {
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm"
        ).map { DateTimeFormatter.ofPattern(it) }

        for (formatter in patterns) {
            try {
                val dateTime = LocalDateTime.parse(value, formatter)
                return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            } catch (_: DateTimeParseException) {
            }
        }
        return value
    }

    return if (meeting != null) "${parseTime(meeting.startTime)} - ${parseTime(meeting.endTime)}" else "Время не указано"
}
