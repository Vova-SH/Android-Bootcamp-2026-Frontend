package ru.sicampus.bootcamp2026.ui.screen.invitations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.InvitationStatus
import ru.sicampus.bootcamp2026.ui.components.JuicyBackground
import ru.sicampus.bootcamp2026.ui.components.JuicyCard
import ru.sicampus.bootcamp2026.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationsScreen(viewModel: InvitationsViewModel) {
    val invitations by viewModel.invitations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadInvitations() }

    JuicyBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Входящие", style = MaterialTheme.typography.displayMedium)
                    IconButton(onClick = { viewModel.loadInvitations() }) {
                        Icon(Icons.Default.Refresh, "Обновить", tint = BrandPrimary)
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrandPrimary)
                } else if (invitations.isEmpty()) {
                    Text("Нет новых приглашений", modifier = Modifier.align(Alignment.Center), color = TextTertiary)
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(invitations) { invitation ->
                            JuicyInvitationItem(
                                invitation = invitation,
                                onAccept = { viewModel.respondToInvitation(invitation.id, true) },
                                onReject = { viewModel.respondToInvitation(invitation.id, false) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JuicyInvitationItem(
    invitation: Invitation,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    JuicyCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Встреча #${invitation.meetingId}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                StatusChip(invitation.status)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Получено: ${invitation.createdAt.take(10)}",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            if (invitation.status == InvitationStatus.AWAITS) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onReject,
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceLight, contentColor = BrandTertiary),
                        shape = MaterialTheme.shapes.small
                    ) { Text("Отклонить") }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) { Text("Принять", color = BrandLight) }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: InvitationStatus) {
    val chipData = when (status) {
        InvitationStatus.ACCEPTED -> Pair(AccentTeal, "Принято")
        InvitationStatus.REJECTED -> Pair(BrandTertiary, "Отклонено")
        InvitationStatus.AWAITS -> Pair(BrandPrimary, "Ожидает")
        InvitationStatus.UNKNOWN -> Pair(Color.Gray, "Неизвестно")
    }

    val color = chipData.first
    val text = chipData.second

    Surface(
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}