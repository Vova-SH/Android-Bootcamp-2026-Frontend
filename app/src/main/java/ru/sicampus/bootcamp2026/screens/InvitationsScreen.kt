package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.components.MainBottomBar
import ru.sicampus.bootcamp2026.components.MainTab
import ru.sicampus.bootcamp2026.components.MainTopBar
import ru.sicampus.bootcamp2026.components.MeetingCard
import ru.sicampus.bootcamp2026.components.MeetingUi
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.White
import ru.sicampus.bootcamp2026.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.data.dto.MeetingTypeDto
import ru.sicampus.bootcamp2026.data.repo.BootcampRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Instant
import java.time.ZoneId

enum class InvitationStatus { Pending, Accepted, Declined }

data class InvitationUi(
    val id: String,
    val date: LocalDate,
    val meeting: MeetingUi,
    val status: InvitationStatus = InvitationStatus.Pending
)

private fun sampleInvitations(today: LocalDate): List<InvitationUi> = listOf(
    InvitationUi(
        id = "1",
        date = today,
        meeting = MeetingUi(
            mode = "Оффлайн",
            place = "Кабинет 138, C2",
            title = "Планирование спринта",
            time = "10:00 – 11:00",
            host = "Team Lead"
        )
    ),
    InvitationUi(
        id = "2",
        date = today.plusDays(1),
        meeting = MeetingUi(
            mode = "Онлайн",
            place = "Zoom",
            title = "Демо по проекту",
            time = "15:00 – 16:00",
            host = "Куратор курса"
        )
    )
)

@Composable
fun InvitationsScreen(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val repo = remember { BootcampRepository() }
    val invitations = remember { mutableStateListOf<InvitationUi>() }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        try {
            val dtos = repo.getMyInvitations()
            val mapped = dtos.map { dto ->
                val start = dto.meetingStartAt?.let { parseIso(it) }
                val end = dto.meetingEndAt?.let { parseIso(it) }

                val date = start?.toLocalDate() ?: today
                val st = start?.toLocalTime()?.toString()?.take(5)
                val en = end?.toLocalTime()?.toString()?.take(5)
                val timeLabel = when {
                    st != null && en != null -> "$st – $en"
                    st != null -> st
                    else -> ""
                }

                val modeLabel = when (dto.meetingType) {
                    MeetingTypeDto.ONLINE -> "Онлайн"
                    MeetingTypeDto.OFFLINE -> "Оффлайн"
                    MeetingTypeDto.HYBRID -> "Гибрид"
                    else -> ""
                }

                val host = listOfNotNull(dto.organizerName, dto.organizerLastname)
                    .joinToString(" ")
                    .ifBlank { "Организатор" }

                val uiMeeting = MeetingUi(
                    mode = modeLabel.ifBlank { "Оффлайн" },
                    place = "",
                    title = "Встреча #${dto.meetingId ?: ""}",
                    time = timeLabel,
                    host = host
                )

                InvitationUi(
                    id = (dto.id ?: 0L).toString(),
                    date = date,
                    meeting = uiMeeting,
                    status = when (dto.status) {
                        InvitationStatusDto.ACCEPTED -> InvitationStatus.Accepted
                        InvitationStatusDto.DECLINED -> InvitationStatus.Declined
                        else -> InvitationStatus.Pending
                    }
                )
            }

            invitations.clear()
            invitations.addAll(mapped)
        } catch (e: Exception) {
            error = e.message ?: "Ошибка загрузки"
            invitations.clear()
            invitations.addAll(sampleInvitations(today))
        } finally {
            isLoading = false
        }
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(136.dp)) }

            if (isLoading) {
                item {
                    Text(
                        text = "Загружаем приглашения...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White
                    )
                }
            }

            if (!error.isNullOrBlank()) {
                item {
                    Text(
                        text = "Не удалось подключиться к backend. Показаны демо-данные.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White
                    )
                }
            }

            if (invitations.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "У вас нет активных приглашений",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White
                        )
                    }
                }
            } else {
                items(invitations, key = { it.id }) { inv ->
                    InvitationCard(
                        invitation = inv,
                        onAccept = {
                            scope.launch {
                                runCatching {
                                    repo.respondToInvitation(inv.id.toLong(), InvitationStatusDto.ACCEPTED)
                                }.onSuccess {
                                    val idx = invitations.indexOfFirst { it.id == inv.id }
                                    if (idx >= 0) invitations[idx] = inv.copy(status = InvitationStatus.Accepted)
                                }.onFailure {
                                    error = it.message ?: "Ошибка"
                                }
                            }
                        },
                        onDecline = {
                            scope.launch {
                                runCatching {
                                    repo.respondToInvitation(inv.id.toLong(), InvitationStatusDto.DECLINED)
                                }.onSuccess {
                                    val idx = invitations.indexOfFirst { it.id == inv.id }
                                    if (idx >= 0) invitations[idx] = inv.copy(status = InvitationStatus.Declined)
                                }.onFailure {
                                    error = it.message ?: "Ошибка"
                                }
                            }
                        }
                    )
                }
            }

            item { Spacer(Modifier.height(96.dp)) }
        }

        MainTopBar(
            title = "Приглашения",
            modifier = Modifier.align(Alignment.TopCenter)
        )

        MainBottomBar(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun InvitationCard(
    invitation: InvitationUi,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Gray)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MeetingCard(meeting = invitation.meeting)

        when (invitation.status) {
            InvitationStatus.Pending -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onAccept,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Gray
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text("Принять")
                    }

                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = White
                        )
                    ) {
                        Text("Не принять")
                    }
                }
            }

            InvitationStatus.Accepted -> {
                Text(
                    text = "Вы приняли приглашение",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )
            }

            InvitationStatus.Declined -> {
                Text(
                    text = "Вы отказались от приглашения",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White
                )
            }
        }
    }
}

private fun parseIso(raw: String): LocalDateTime? {
    val t = raw.trim()
    if (t.isBlank()) return null

    return runCatching { LocalDateTime.parse(t) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(t).toLocalDateTime() }.getOrNull()
        ?: runCatching { Instant.parse(t).atZone(ZoneId.systemDefault()).toLocalDateTime() }.getOrNull()
}
