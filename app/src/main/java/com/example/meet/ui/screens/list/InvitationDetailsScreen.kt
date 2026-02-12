package com.example.meet.ui.screens.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet.R
import com.example.meet.data.dto.InvitationDto
import com.example.meet.data.dto.InvitationResponseStatus
import com.example.meet.data.dto.MeetingDto
import com.example.meet.data.dto.UserDto
import com.example.meet.data.source.DataLocator
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun InvitationDetailsScreen(navController: NavHostController, invitationId: Int) {
    val invDs = remember { DataLocator.invitationDataSource }
    val meetDs = remember { DataLocator.meetingDataSource }
    val userDs = remember { DataLocator.userInfoDataSource }
    val scope = rememberCoroutineScope()

    var invitation by remember { mutableStateOf<InvitationDto?>(null) }
    var meeting by remember { mutableStateOf<MeetingDto?>(null) }
    var participants by remember { mutableStateOf<List<UserDto>>(emptyList()) }
    var organizer by remember { mutableStateOf<UserDto?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var showDeclineDialog by remember { mutableStateOf(false) }
    var declineComment by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }


    suspend fun loadInvitationData() {
        try {
            val inv = invDs.getInvitationById(invitationId).getOrThrow()
            invitation = inv
            val meet = meetDs.getMeetingById(inv.meetingId.toInt()).getOrNull()
            meeting = meet

            //загруз участ
            if (meet?.participantIds?.isNotEmpty() == true) {
                val allUsers = userDs.loadAllUsers()
                participants = allUsers.filter { user ->
                    meet.participantIds.contains(user.id)
                }
            }

            //загрузка орган.
            meet?.organizerId?.let { orgId ->
                try {
                    organizer = userDs.getUserById(orgId)
                } catch (_: Exception) {
                    //поиск органа
                }
            }
        } catch (e: Exception) {
            error = "Не удалось загрузить приглашение: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    //загрузка
    LaunchedEffect(invitationId) {
        loadInvitationData()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Детали приглашения",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Назад",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Загрузка деталей приглашения...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Ошибка",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.error
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
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = error.orEmpty(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                error = null
                                loadInvitationData()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Повторить")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                //загол
                                Text(
                                    text = meeting?.title ?: "Встреча #${invitation?.meetingId ?: invitationId}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                //описа
                                meeting?.description?.takeIf { it.isNotBlank() }?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                //стас
                                val statusText = when (invitation?.responseStatus?.uppercase()) {
                                    InvitationResponseStatus.PENDING -> "Ожидание"
                                    InvitationResponseStatus.ACCEPTED -> "Принято"
                                    InvitationResponseStatus.DECLINED -> "Отклонено"
                                    else -> invitation?.responseStatus ?: "Неизвестно"
                                }
                                val statusColor = when (invitation?.responseStatus?.uppercase()) {
                                    InvitationResponseStatus.PENDING -> MaterialTheme.colorScheme.primary
                                    InvitationResponseStatus.ACCEPTED -> MaterialTheme.colorScheme.tertiary
                                    InvitationResponseStatus.DECLINED -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.secondary
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(statusColor)
                                    )
                                    Text(
                                        text = "Статус: $statusText",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                //организ
                                organizer?.let { org ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_person),
                                            contentDescription = "Организатор",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Column {
                                            Text(
                                                text = "Организатор: ${org.fullName}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            if (!org.position.isNullOrBlank()) {
                                                Text(
                                                    text = org.position,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }

                                //тайм и встречи
                                val timeText = meeting?.let {
                                    formatMeetingTime(it)
                                } ?: "Время не указано"

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_clock),
                                        contentDescription = "Время",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = timeText,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                //приотет
                                val priorityColor = when (meeting?.meetingPriority) {
                                    "HIGH" -> MaterialTheme.colorScheme.error
                                    "MEDIUM" -> MaterialTheme.colorScheme.primary
                                    "LOW" -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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


                                meeting?.location?.takeIf { it.isNotBlank() }?.let { loc ->
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Место/ссылка:",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = loc,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                //кнопка
                                if (invitation?.responseStatus == InvitationResponseStatus.PENDING) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    try {
                                                        invDs.updateInvitationResponse(
                                                            invitationId,
                                                            InvitationResponseStatus.ACCEPTED
                                                        )
                                                        //локальное состояние
                                                        invitation = invitation?.copy(
                                                            responseStatus = InvitationResponseStatus.ACCEPTED
                                                        )
                                                    } catch (e: Exception) {
                                                        error = "Не удалось принять приглашение: ${e.message}"
                                                    }
                                                }
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
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
                                                    scope.launch {
                                                        try {
                                                            invDs.updateInvitationResponse(
                                                                invitationId = invitationId,
                                                                responseStatus = InvitationResponseStatus.DECLINED
                                                            )
                                                            invitation = invitation?.copy(
                                                                responseStatus = InvitationResponseStatus.DECLINED
                                                            )
                                                        } catch (e: Exception) {
                                                            error = "Не удалось отклонить приглашение: ${e.message}"
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("Отклонить")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //лист участ
                    item {
                        Text(
                            text = "Участники встречи (${participants.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }

                    if (participants.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_person),
                                        contentDescription = "Нет участников",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Участники не указаны",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        items(participants) { participant ->
                            ParticipantCard(participant = participant)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    //диАлог
    if (showDeclineDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                invDs.updateInvitationResponse(
                                    invitationId = invitationId,
                                    responseStatus = InvitationResponseStatus.DECLINED,
                                    comment = declineComment.ifBlank { null }
                                )
                                invitation = invitation?.copy(
                                    responseStatus = InvitationResponseStatus.DECLINED
                                )
                            } catch (e: Exception) {
                                error = "Не удалось отклонить приглашение: ${e.message}"
                            }
                        }
                    }
                ) {
                    Text("Отклонить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { }
                ) {
                    Text("Отмена")
                }
            },
            title = { Text("Причина отклонения приглашения") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Напишите причину отклонения приглашения (необязательно)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
private fun ParticipantCard(participant: UserDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //ава
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = participant.fullName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Информация
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = participant.fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                if (!participant.position.isNullOrBlank()) {
                    Text(
                        text = participant.position,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!participant.department.isNullOrBlank()) {
                    Text(
                        text = participant.department,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

private fun formatMeetingTime(meeting: MeetingDto): String {
    fun parseTime(value: String): String {
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
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

    return "${parseTime(meeting.startTime)} - ${parseTime(meeting.endTime)}"
}