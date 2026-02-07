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
import com.example.meet.data.source.DataLocator
import com.example.meet.ui.theme.BlackGroundColor
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

sealed class InvitationsListUiState {
    data object Loading : InvitationsListUiState()
    data class Success(val meetings: List<MeetingDto>) : InvitationsListUiState()
    data class Error(val message: String) : InvitationsListUiState()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun InvitationsListScreen(navController: NavHostController) {
    val ds = remember { DataLocator.userInfoDataSource }
    val scope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf<InvitationsListUiState>(InvitationsListUiState.Loading) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val meetings = ds.loadMeetingsForCurrentUser()
                uiState = InvitationsListUiState.Success(meetings)
            } catch (e: Exception) {
                uiState = InvitationsListUiState.Error("Не удалось загрузить приглашения: ${e.message}")
            }
        }
    }

    val primaryDarkBlue = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val surfaceColor = MaterialTheme.colorScheme.background
    val errorColor = MaterialTheme.colorScheme.error
    val errorContainer = MaterialTheme.colorScheme.errorContainer
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

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
                                    val meetings = ds.loadMeetingsForCurrentUser()
                                    uiState = InvitationsListUiState.Success(meetings)
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
                    InvitationsContent(
                        meetings = state.meetings,
                        primaryDarkBlue = primaryDarkBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun InvitationsContent(
    meetings: List<MeetingDto>,
    primaryDarkBlue: Color
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
    ) {
        item {
            InvitationsHeaderSection(
                meetingsCount = meetings.size,
                primaryDarkBlue = primaryDarkBlue
            )
        }

        if (meetings.isEmpty()) {
            item {
                EmptyInvitationsSection(
                    primaryDarkBlue = primaryDarkBlue
                )
            }
        } else {
            items(meetings) { meeting ->
                InvitationCard(
                    meeting = meeting,
                    primaryDarkBlue = primaryDarkBlue
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
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
                    text = "Всего активных: $meetingsCount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun InvitationCard(
    meeting: MeetingDto,
    primaryDarkBlue: Color
) {
    val statusColor = when (meeting.status) {
        "SCHEDULED", "PLANNED" -> MaterialTheme.colorScheme.primary
        "COMPLETED" -> MaterialTheme.colorScheme.tertiary
        "CANCELLED" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.secondary
    }

    val priorityColor = when (meeting.meetingPriority) {
        "HIGH" -> MaterialTheme.colorScheme.error
        "MEDIUM" -> MaterialTheme.colorScheme.primary
        "LOW" -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
                            text = meeting.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryDarkBlue
                        )

                        if (meeting.description?.isNotBlank() == true) {
                            Text(
                                text = meeting.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
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
                            text = when (meeting.status) {
                                "SCHEDULED", "PLANNED" -> "Ожидает"
                                "COMPLETED" -> "Завершена"
                                "CANCELLED" -> "Отменена"
                                else -> meeting.status
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
                            color = MaterialTheme.colorScheme.onSurface
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
                                text = when (meeting.meetingPriority) {
                                    "HIGH" -> "Высокий приоритет"
                                    "MEDIUM" -> "Средний приоритет"
                                    "LOW" -> "Низкий приоритет"
                                    else -> meeting.meetingPriority
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
                    Button(
                        onClick = { /* Принять приглашение */ },
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
                        onClick = { /* Отклонить приглашение */ },
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

private fun formatMeetingTime(meeting: MeetingDto): String {
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

    return "${parseTime(meeting.startTime)} - ${parseTime(meeting.endTime)}"
}