package com.teto.planner.presentation.features.invitations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.user.UserSummary
import com.teto.planner.presentation.common.openTelegramChat
import com.teto.planner.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun InvitationScreen(
    viewModel: InvitationViewModel,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val actionError by viewModel.actionError.collectAsState()

    if (actionError != null) {
        val errorData = actionError!!
        ErrorInvitationDialog(
            message = errorData.message,
            organizer = errorData.organizer,
            onDismiss = { viewModel.clearErrorAction() }
        )
    }

    InvitationContent(
        uiState = uiState,
        onProfileClick = onProfileClick,
        onRefresh = { viewModel.loadInvitations() },
        onResponse = { invitation, accepted ->
            viewModel.onInvitationResponse(invitation, accepted)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationContent(
    uiState: InvitationUiState,
    onProfileClick: () -> Unit,
    onRefresh: () -> Unit,
    onResponse: (Invitation, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Приглашения",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Профиль")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onRefresh,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Обновить")
            }
        }
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is InvitationUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    is InvitationUiState.Empty -> EmptyState()
                    is InvitationUiState.Error -> Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                    is InvitationUiState.Success -> {
                        val invitations = uiState.invitations
                        val topInvitation = invitations.firstOrNull()

                        invitations.reversed().forEach { invitation ->
                            SwipeableCardItem(
                                invitation = invitation,
                                onSwiped = { accepted -> onResponse(invitation, accepted) },
                                isTopCard = invitation == topInvitation
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeableCardItem(
    invitation: Invitation,
    onSwiped: (Boolean) -> Unit,
    isTopCard: Boolean
) {
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val scope = rememberCoroutineScope()
    val meeting = invitation.meeting

    val swipeThreshold = 300f
    val maxRotation = 15f

    val bannerColors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )
    val bannerBackground = remember { bannerColors.random() }
    val bannerContentColor = when(bannerBackground) {
        MaterialTheme.colorScheme.primaryContainer -> MaterialTheme.colorScheme.onPrimaryContainer
        MaterialTheme.colorScheme.secondaryContainer -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    val rotation by derivedStateOf {
        (offset.value.x / 60).coerceIn(-maxRotation, maxRotation)
    }

    val acceptColor = MaterialTheme.colorScheme.primary
    val declineColor = MaterialTheme.colorScheme.error

    val overlayColor by derivedStateOf {
        when {
            offset.value.x > 50 -> acceptColor.copy(alpha = (offset.value.x / 500f).coerceIn(0f, 0.5f))
            offset.value.x < -50 -> declineColor.copy(alpha = (abs(offset.value.x) / 500f).coerceIn(0f, 0.5f))
            else -> Color.Transparent
        }
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
            .graphicsLayer { rotationZ = rotation }
            .pointerInput(isTopCard) {
                if (!isTopCard) return@pointerInput
                detectDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (offset.value.x > swipeThreshold) {
                                offset.animateTo(Offset(1500f, 0f), tween(300))
                                onSwiped(true)
                            } else if (offset.value.x < -swipeThreshold) {
                                offset.animateTo(Offset(-1500f, 0f), tween(300))
                                onSwiped(false)
                            } else {
                                offset.animateTo(Offset.Zero, tween(400))
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch { offset.snapTo(offset.value + dragAmount) }
                    }
                )
            }
            .fillMaxWidth()
            .height(450.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(bannerBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = bannerContentColor,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = meeting.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    InfoRow(icon = Icons.Default.Person, text = meeting.organizer.name)

                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    InfoRow(
                        icon = Icons.Default.DateRange,
                        text = "${meeting.date}, ${meeting.startTime.format(timeFormatter)} - ${meeting.endTime.format(timeFormatter)}"
                    )

                    meeting.room?.let { room ->
                        InfoRow(icon = Icons.Default.LocationOn, text = room.name)
                    } ?: InfoRow(icon = Icons.Default.LocationOn, text = "Место не указано")
                }

                Text(
                    text = "Свайпни вправо, чтобы принять",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(overlayColor)
        )

        if (overlayColor != Color.Transparent) {
            val isRight = offset.value.x > 0
            Icon(
                imageVector = if (isRight) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Новых приглашений нет",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Как только кто-то позовет вас на встречу, она появится здесь.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun ErrorInvitationDialog(
    message: String,
    organizer: UserSummary?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Ошибка",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                if (organizer?.telegram != null && organizer.telegram.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { context.openTelegramChat(organizer.telegram) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Написать организатору")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(if (organizer == null) "Обновить" else "Ок")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    )
}

@Preview(showSystemUi = true)
@Composable
fun InvitationScreenPreview() {
    AppTheme {
        InvitationContent(
            uiState = InvitationUiState.Empty,
            onProfileClick = {},
            onResponse = { _, _ -> },
            onRefresh = {}
        )
    }
}