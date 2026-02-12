package com.example.meet.ui.screens.meetings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.meet.R
import com.example.meet.data.dto.InvitationDto
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
fun MeetingDetailsScreen(navController: NavHostController, meetingId: Int) {
    val ds = remember { DataLocator.meetingDataSource }
    val userDs = remember { DataLocator.userInfoDataSource }
    val invitationDs = remember { DataLocator.invitationDataSource }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var meeting by remember { mutableStateOf<MeetingDto?>(null) }
    var participants by remember { mutableStateOf<List<UserDto>>(emptyList()) }
    var invitations by remember { mutableStateOf<List<InvitationDto>>(emptyList()) }
    var organizer by remember { mutableStateOf<UserDto?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isLoadingParticipants by remember { mutableStateOf(false) }

    suspend fun loadMeetingData() {
        try {
            println("DEBUG: Начинаем загрузку встречи с ID: $meetingId")
            val result = ds.getMeetingById(meetingId)
            meeting = result.getOrNull()

            if (meeting == null) {
                error = "Не удалось загрузить встречу"
            } else {
                println("DEBUG: Загружена встреча: ${meeting!!.title}")
                println("DEBUG: Meeting ID: ${meeting!!.id}")

                meeting!!.organizerId?.let { orgId ->
                    try {
                        organizer = userDs.getUserById(orgId.toLong())
                        println("DEBUG: Загружен организатор: ${organizer?.fullName} (id=$orgId)")
                    } catch (e: Exception) {
                        println("DEBUG: Не удалось загрузить организатора: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            error = "Не удалось загрузить встречу: ${e.message}"
            println("DEBUG: Ошибка при загрузке встречи: ${e.message}")
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    suspend fun loadParticipantsViaInvitations() {
        if (meeting == null) {
            println("DEBUG: Встреча не загружена, пропускаем загрузку участников")
            return
        }

        val currentMeetingId = meeting!!.id
        println("DEBUG: Загружаем участников для встречи ID: $currentMeetingId")

        isLoadingParticipants = true

        try {
            println("DEBUG: Загружаем приглашения для встречи $currentMeetingId...")
            val result = invitationDs.getInvitationsByMeetingId(currentMeetingId)

            if (result.isFailure) {
                println("DEBUG: Ошибка загрузки приглашений: ${result.exceptionOrNull()?.message}")
                try {
                    val allInvitations = invitationDs.loadAllInvitations()
                    val meetingInvitations = allInvitations.filter { it.meetingId == currentMeetingId }
                    invitations = meetingInvitations
                    println("DEBUG: Найдено ${meetingInvitations.size} приглашений (через общую загрузку)")
                } catch (e: Exception) {
                    println("DEBUG: Ошибка при загрузке всех приглашений: ${e.message}")
                    participants = emptyList()
                    return
                }
            } else {
                val meetingInvitations = result.getOrThrow()
                invitations = meetingInvitations
                println("DEBUG: Найдено ${meetingInvitations.size} приглашений")
            }

            if (invitations.isEmpty()) {
                println("DEBUG: Нет приглашений для этой встречи, пробуем fallback по participantIds")
                val idsFromMeeting = meeting?.participantIds?.map { it.toInt() }?.distinct().orEmpty()
                if (idsFromMeeting.isEmpty()) {
                    participants = emptyList()
                    return
                }
                val loadedParticipants = mutableListOf<UserDto>()
                val allUsers = try {
                    userDs.loadAllUsers()
                } catch (e: Exception) {
                    println("DEBUG: Ошибка загрузки всех пользователей: ${e.message}")
                    emptyList()
                }
                if (allUsers.isNotEmpty()) {
                    loadedParticipants.addAll(allUsers.filter { u -> idsFromMeeting.contains(u.id.toInt()) })
                } else {
                    for (userId in idsFromMeeting) {
                        try {
                            val user = userDs.getUserById(userId.toLong())
                            loadedParticipants.add(user)
                        } catch (e: Exception) {
                            println("DEBUG: Ошибка загрузки пользователя $userId: ${e.message}")
                        }
                    }
                }
                participants = loadedParticipants
                return
            }

            val userIds = invitations.mapNotNull { it.userId }.distinct()
            println("DEBUG: ID участников из приглашений: $userIds")

            if (userIds.isEmpty()) {
                participants = emptyList()
                return
            }

            val loadedParticipants = mutableListOf<UserDto>()

            val allUsers = try {
                userDs.loadAllUsers()
            } catch (e: Exception) {
                println("DEBUG: Ошибка загрузки всех пользователей: ${e.message}")
                emptyList()
            }

            if (allUsers.isNotEmpty()) {
                loadedParticipants.addAll(allUsers.filter { user ->
                    userIds.contains(user.id).also { found ->
                        if (found) println("DEBUG: Найден участник: ${user.fullName} (id=${user.id})")
                    }
                })
            } else {
                for (userId in userIds) {
                    try {
                        val user = userDs.getUserById(userId)
                        loadedParticipants.add(user)
                        println("DEBUG: Загружен участник: ${user.fullName} (id=$userId)")
                    } catch (e: Exception) {
                        println("DEBUG: Ошибка загрузки пользователя $userId: ${e.message}")
                    }
                }
            }

            participants = loadedParticipants
            println("DEBUG: Успешно загружено ${participants.size} участников")

        } catch (e: Exception) {
            println("DEBUG: Ошибка при загрузке участников через приглашения: ${e.message}")
            e.printStackTrace()
        } finally {
            isLoadingParticipants = false
        }
    }

    LaunchedEffect(meetingId) {
        loadMeetingData()
    }

    LaunchedEffect(meeting) {
        if (meeting != null) {
            loadParticipantsViaInvitations()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Детали встречи",
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
                        text = "Загрузка деталей встречи...",
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
                                loadMeetingData()
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
                                Text(
                                    text = meeting?.title ?: "Встреча #$meetingId",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                meeting?.description?.takeIf { it.isNotBlank() }?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                val statusColor = when (meeting?.status?.uppercase()) {
                                    "SCHEDULED", "PLANNED" -> MaterialTheme.colorScheme.primary
                                    "COMPLETED" -> MaterialTheme.colorScheme.tertiary
                                    "CANCELLED" -> MaterialTheme.colorScheme.error
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
                                        text = when (meeting?.status?.uppercase()) {
                                            "SCHEDULED", "PLANNED" -> "Запланировано"
                                            "COMPLETED" -> "Завершено"
                                            "CANCELLED" -> "Отменено"
                                            else -> meeting?.status ?: "Неизвестно"
                                        },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                organizer?.let { org ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
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

                                val priorityColor = when (meeting?.meetingPriority?.uppercase()) {
                                    "HIGH" -> MaterialTheme.colorScheme.error
                                    "MEDIUM" -> MaterialTheme.colorScheme.primary
                                    "LOW" -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Приоритет",
                                        modifier = Modifier.size(16.dp),
                                        tint = priorityColor
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(priorityColor.copy(alpha = 0.1f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = when (meeting?.meetingPriority?.uppercase()) {
                                                "HIGH" -> "Высокий"
                                                "MEDIUM" -> "Средний"
                                                "LOW" -> "Низкий"
                                                else -> meeting?.meetingPriority ?: "Неизвестно"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = priorityColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Время",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                                            .padding(12.dp)
                                    ) {
                                        val timeText = meeting?.let {
                                            val parsed = parseMeetingDateTime(it)
                                            parsed?.let { (start, end) ->
                                                formatMeetingTime(start, end)
                                            } ?: "${it.startTime} - ${it.endTime}"
                                        } ?: ""

                                        Column {
                                            Text(
                                                text = "Дата и время",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = timeText,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }

                                meeting?.location?.takeIf { it.isNotBlank() }?.let { loc ->
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = "Место",
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "Место проведения",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Text(text = loc, style = MaterialTheme.typography.bodyMedium)
                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            val isLink = loc.startsWith("http", ignoreCase = true)
                                            val buttonText = if (isLink) "Открыть ссылку" else "Открыть карту"
                                            Button(
                                                onClick = {
                                                    if (isLink) {
                                                        val intent = Intent(Intent.ACTION_VIEW, loc.toUri())
                                                        context.startActivity(intent)
                                                    } else {
                                                        val uri = ("geo:0,0?q=" + Uri.encode(loc)).toUri()
                                                        val intent = Intent(Intent.ACTION_VIEW, uri)
                                                        context.startActivity(intent)
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary,
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Text(buttonText)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Участники встречи",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                if (isLoadingParticipants) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Загрузка...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "(${participants.size})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            if (invitations.isNotEmpty()) {
                                Text(
                                    text = "Найдено ${invitations.size} приглашений",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (isLoadingParticipants) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Загрузка участников...",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else if (participants.isEmpty()) {
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
                                        text = "Участники не найдены",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Приглашения могут быть созданы отдельно",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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

                        if (!isLoading && !isLoadingParticipants) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        if (meeting != null) {
                                            isLoadingParticipants = true
                                            loadParticipantsViaInvitations()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.background
                                )
                            ) {
                                Text("Обновить список участников")
                            }
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ParticipantCard(participant: UserDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                // TODO: Добавить переход в профиль пользователя
                println("Переход в профиль пользователя: ${participant.id}")
            },
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
                val initials = participant.fullName
                    .split(" ")
                    .take(2).joinToString("") { it.take(1).uppercase() }

                Text(
                    text = initials.ifEmpty { participant.fullName.take(1).uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

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

                Text(
                    text = "ID: ${participant.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

private fun parseMeetingDateTime(meeting: MeetingDto): Pair<LocalDateTime, LocalDateTime>? {
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm"
    ).map { DateTimeFormatter.ofPattern(it) }

    fun parse(value: String): LocalDateTime? {
        for (formatter in patterns) {
            try {
                return LocalDateTime.parse(value, formatter)
            } catch (_: DateTimeParseException) {
                continue
            }
        }
        return null
    }

    val start = parse(meeting.startTime)
    val end = parse(meeting.endTime)
    if (start != null && end != null) return start to end
    return null
}

private fun formatMeetingTime(start: LocalDateTime, end: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return "${start.format(dateFormatter)} ${start.format(timeFormatter)} - ${end.format(timeFormatter)}"
}
