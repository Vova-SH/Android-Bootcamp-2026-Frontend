package com.example.meet.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet.R
import kotlinx.coroutines.launch
import com.example.meet.data.dto.NotificationDto
import com.example.meet.data.source.DataLocator
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

sealed class NotificationsUiState {
    data object Loading : NotificationsUiState()
    data class Success(val notifications: List<NotificationDto>) : NotificationsUiState()
    data class Error(val message: String) : NotificationsUiState()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun NotificationsScreen(navController: NavHostController) {
    val ds = remember { DataLocator.userInfoDataSource }
    val invDs = remember { DataLocator.invitationDataSource }
    val scope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    var uiState by remember { mutableStateOf<NotificationsUiState>(NotificationsUiState.Loading) }
    var notificationsViewed by remember { mutableStateOf(false) }

    fun loadNotifications() {
        scope.launch {
            try {
                val notifications = ds.loadNotifications()
                uiState = NotificationsUiState.Success(notifications)
            } catch (e: Exception) {
                uiState = NotificationsUiState.Error("Не удалось загрузить уведомления: ${e.message}")
            }
        }
    }

    LaunchedEffect(Unit) {
        loadNotifications()
        notificationsViewed = true
    }
    LaunchedEffect(uiState, notificationsViewed) {
        if (notificationsViewed) {
            val state = uiState
            if (state is NotificationsUiState.Success) {
                val hasUnread = state.notifications.any { !it.isRead }
                if (hasUnread) {
                    uiState = NotificationsUiState.Success(
                        state.notifications.map { it.copy(isRead = true) }
                    )
                    state.notifications.forEach { n ->
                        if (!n.isRead) {
                            runCatching { ds.markNotificationAsRead(n.id) }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Уведомления",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onPrimary
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
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.background
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Назад",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { loadNotifications() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.background
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_reset),
                            contentDescription = "Обновить"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorScheme.primary,
                    titleContentColor = colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            when (val state = uiState) {
                NotificationsUiState.Loading -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = colorScheme.primary
                    )
                    Text(
                        text = "Загрузка уведомлений...",
                        color = colorScheme.onSurfaceVariant
                    )
                }

                is NotificationsUiState.Error -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Ошибка",
                            modifier = Modifier.size(40.dp),
                            tint = colorScheme.error
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
                            color = colorScheme.error
                        )
                        Text(
                            text = state.message,
                            color = colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                    Button(
                        onClick = {
                            loadNotifications()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.errorContainer,
                            contentColor = colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Повторить")
                    }
                }

                is NotificationsUiState.Success -> {
                    NotificationsContent(
                        notifications = state.notifications,
                        viewed = notificationsViewed,
                        onNotificationClick = { notification ->
                            // Пометка уведомления как прочитанного
                            scope.launch {
                                try {
                                    val updatedNotifications = state.notifications.map { n ->
                                        if (n.id == notification.id) {
                                            n.copy(isRead = true)
                                        } else {
                                            n
                                        }
                                    }
                                    uiState = NotificationsUiState.Success(updatedNotifications)

                                    try {
                                        ds.markNotificationAsRead(notification.id)
                                    } catch (e: NoSuchMethodError) {
                                        println("Метод markNotificationAsRead не реализован")
                                    } catch (e: Exception) {
                                        println("Ошибка при отправке на сервер: $e")
                                    }

                                    if (notification.type == "MEETING_INVITATION" && notification.meetingId != null) {
                                        runCatching {
                                            val user = ds.loadCurrentUser()
                                            val invitations = invDs.getInvitations(user.id.toInt()).getOrThrow()
                                            val match = invitations.firstOrNull {
                                                it.meetingId == notification.meetingId
                                            }
                                            if (match != null) {
                                                navController.navigate("invitation_details/${match.id}")
                                            } else {
                                                navController.navigate("meeting_details/${notification.meetingId?.toInt() ?: 0}")
                                            }
                                        }.onFailure {
                                            navController.navigate("meeting_details/${notification.meetingId?.toInt() ?: 0}")
                                        }
                                    }
                                } catch (e: Exception) {
                                    println("Ошибка при обработке уведомления: $e")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalSerializationApi
@Composable
private fun NotificationsContent(
    notifications: List<NotificationDto>,
    viewed: Boolean,
    onNotificationClick: (NotificationDto) -> Unit
) {
    val ds = remember { DataLocator.userInfoDataSource }
    val invDs = remember { DataLocator.invitationDataSource }
    val meetDs = remember { DataLocator.meetingDataSource }
    var statusByMeetingId by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }
    var formatByMeetingId by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }
    LaunchedEffect(notifications) {
        runCatching {
            val user = ds.loadCurrentUser()
            val invitations = invDs.getInvitations(user.id.toInt()).getOrThrow()
            statusByMeetingId = invitations.associate { it.meetingId to it.responseStatus }
            val meetingIds = notifications
                .filter { it.type == "MEETING_INVITATION" && it.meetingId != null }
                .mapNotNull { it.meetingId }
                .distinct()
            val map = mutableMapOf<Long, String>()
            for (id in meetingIds) {
                runCatching {
                    val meeting = meetDs.getMeetingById(id.toInt()).getOrThrow()
                    val isOnline = meeting.location?.let { loc ->
                        val l = loc.lowercase()
                        l.contains("http") || l.contains("zoom") || l.contains("meet") || l.contains("teams")
                    } ?: (meeting.roomId == null)
                    map[id] = if (isOnline) "онлайн" else "офлайн"
                }
            }
            formatByMeetingId = map
        }
    }
    val unreadCount = notifications.count { !it.isRead }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
    ) {
        item {
            NotificationsHeaderSection(unreadCount = unreadCount)
        }

        if (notifications.isEmpty()) {
            item {
                EmptyNotificationsSection()
            }
        } else {
            items(notifications) { notification ->
                NotificationCard(
                    notification = notification,
                    invitationStatus = notification.meetingId?.let { statusByMeetingId[it] },
                    meetingFormat = notification.meetingId?.let { formatByMeetingId[it] },
                    viewed = viewed,
                    onClick = { onNotificationClick(notification) }
                )
            }
        }
    }
}

@Composable
private fun NotificationsHeaderSection(unreadCount: Int) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
                    text = "Уведомления",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
                Text(
                    text = when (unreadCount) {
                        0 -> "Все уведомления прочитаны"
                        1 -> "1 непрочитанное уведомление"
                        in 2..4 -> "$unreadCount непрочитанных уведомления"
                        else -> "$unreadCount непрочитанных уведомлений"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = unreadCount.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun EmptyNotificationsSection() {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = "Нет уведомлений",
                    modifier = Modifier.size(36.dp),
                    tint = colorScheme.primary
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Нет уведомлений",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.primary
                )
                Text(
                    text = "Здесь появятся уведомления о встречах и событиях",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationDto,
    invitationStatus: String? = null,
    meetingFormat: String? = null,
    viewed: Boolean = false,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val stripeColor = if (notification.type == "MEETING_INVITATION" && invitationStatus != null) {
        when (invitationStatus.uppercase()) {
            com.example.meet.data.dto.InvitationResponseStatus.PENDING -> colorScheme.primary
            com.example.meet.data.dto.InvitationResponseStatus.ACCEPTED -> colorScheme.tertiary
            com.example.meet.data.dto.InvitationResponseStatus.DECLINED -> colorScheme.secondary
            else -> getNotificationColor(notification.type)
        }
    } else {
        getNotificationColor(notification.type)
    }

    // Фон карточки в зависимости от статуса приглашения
    val containerColor = if (notification.type == "MEETING_INVITATION" && invitationStatus != null) {
        when (invitationStatus.uppercase()) {
            com.example.meet.data.dto.InvitationResponseStatus.PENDING -> colorScheme.primary.copy(alpha = 0.08f)
            com.example.meet.data.dto.InvitationResponseStatus.ACCEPTED -> colorScheme.tertiary.copy(alpha = 0.08f)
            com.example.meet.data.dto.InvitationResponseStatus.DECLINED -> colorScheme.secondary.copy(alpha = 0.08f)
            else -> if (!notification.isRead) colorScheme.surface else colorScheme.surfaceVariant
        }
    } else {
        if (!notification.isRead) colorScheme.surface else colorScheme.surfaceVariant
    }

    val icon = getNotificationIcon(notification.type)
    val iconColor = stripeColor

    val showDot = if (notification.type == "MEETING_INVITATION") {
        invitationStatus?.uppercase() == com.example.meet.data.dto.InvitationResponseStatus.PENDING && !viewed
    } else {
        !notification.isRead && !viewed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(stripeColor)
            )

            Spacer(modifier = Modifier.width(12.dp))


            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Тип уведомления",
                    modifier = Modifier.size(20.dp),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!notification.isRead) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = formatNotificationTime(notification.createdAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(stripeColor.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = when (notification.type) {
                                "MEETING_INVITATION" -> "Приглашение на ${meetingFormat ?: "встречу"}"
                                "MEETING_UPDATE" -> "Изменение встречи"
                                "MEETING_REMINDER" -> "Напоминание"
                                "SYSTEM" -> "Системное"
                                else -> notification.type
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = stripeColor,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (notification.type == "MEETING_INVITATION" && invitationStatus != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(stripeColor.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = when (invitationStatus.uppercase()) {
                                    com.example.meet.data.dto.InvitationResponseStatus.PENDING -> "Ожидание"
                                    com.example.meet.data.dto.InvitationResponseStatus.ACCEPTED -> "Принято"
                                    com.example.meet.data.dto.InvitationResponseStatus.DECLINED -> "Отклонено"
                                    else -> invitationStatus
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = stripeColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (showDot) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(colorScheme.primary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getNotificationIcon(type: String): ImageVector {
    return when (type) {
        "MEETING_INVITATION" -> Icons.Filled.PlayArrow
        "MEETING_UPDATE" -> Icons.Filled.Info
        "MEETING_REMINDER" -> Icons.Filled.Warning
        "SYSTEM" -> Icons.Filled.CheckCircle
        else -> Icons.Filled.Info
    }
}

@Composable
private fun getNotificationColor(type: String): Color {
    val colorScheme = MaterialTheme.colorScheme
    return when (type) {
        "MEETING_INVITATION" -> colorScheme.primary
        "MEETING_UPDATE" -> colorScheme.secondary
        "MEETING_REMINDER" -> colorScheme.tertiary
        "SYSTEM" -> colorScheme.secondary
        else -> colorScheme.onSurfaceVariant
    }
}

private fun formatNotificationTime(timestamp: String): String {
    return try {
        val parsedTime = parseTimestamp(timestamp) ?: return timestamp

        val now = Instant.now()
        val duration = Duration.between(parsedTime, now)

        when {
            duration.toMinutes() < 1 -> "только что"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} ${minutesText(duration.toMinutes().toInt())} назад"
            duration.toHours() < 24 -> "${duration.toHours()} ${hoursText(duration.toHours().toInt())} назад"
            else -> {
                val days = ChronoUnit.DAYS.between(
                    parsedTime.atZone(ZoneId.systemDefault()).toLocalDate(),
                    LocalDate.now()
                )
                when (days) {
                    1L -> "вчера в ${formatTime(parsedTime)}"
                    in 2..6 -> "${days} ${daysText(days.toInt())} назад в ${formatTime(parsedTime)}"
                    else -> formatDate(parsedTime)
                }
            }
        }
    } catch (e: Exception) {
        timestamp
    }
}

private fun parseTimestamp(timestamp: String): Instant? {
    val instant = try {
        Instant.parse(timestamp)
    } catch (e: Exception) {
        null
    }
    if (instant != null) return instant

    val formatters = listOf(
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    )

    for (formatter in formatters) {
        try {
            val localDateTime = LocalDateTime.parse(timestamp, formatter)
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        } catch (_: Exception) {
            continue
        }
    }
    return null
}

private fun formatTime(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}

private fun formatDate(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}

private fun minutesText(minutes: Int): String {
    return when {
        minutes % 10 == 1 && minutes % 100 != 11 -> "минуту"
        minutes % 10 in 2..4 && (minutes % 100 !in 10..<20) -> "минуты"
        else -> "минут"
    }
}

private fun hoursText(hours: Int): String {
    return when {
        hours % 10 == 1 && hours % 100 != 11 -> "час"
        hours % 10 in 2..4 && (hours % 100 !in 10..<20) -> "часа"
        else -> "часов"
    }
}

private fun daysText(days: Int): String {
    return when {
        days % 10 == 1 && days % 100 != 11 -> "день"
        days % 10 in 2..4 && (days % 100 !in 10..<20) -> "дня"
        else -> "дней"
    }
}
