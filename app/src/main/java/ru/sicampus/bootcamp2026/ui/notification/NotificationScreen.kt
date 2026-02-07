package ru.sicampus.bootcamp2026.ui.notification
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sicampus.bootcamp2026.ui.components.InvitationCard
import ru.sicampus.bootcamp2026.ui.theme.LightGreen
import java.time.format.DateTimeFormatter
@Composable
fun NotificationScreen(
    onNavigateToDetails: (String) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sidePadding = 24.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("My ")
                }
                withStyle(style = SpanStyle(color = LightGreen)) {
                    append("invitations")
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 48.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = sidePadding, top = 16.dp, bottom = 8.dp)
        )
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LightGreen)
                }
            }
            state.error != null -> {
                val isConnectionError = state.error!!.contains("Connection", ignoreCase = true) ||
                        state.error!!.contains("timeout", ignoreCase = true) ||
                        state.error!!.contains("unreachable", ignoreCase = true) ||
                        state.error!!.contains("failed to connect", ignoreCase = true) ||
                        state.error!!.contains("NetworkException", ignoreCase = true)

                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isConnectionError) "the server is not responding" else state.error!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            state.invitations.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No invitations",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.invitations) { invitation ->
                        Box(modifier = Modifier.padding(horizontal = sidePadding)) {
                            InvitationCard(
                                title = invitation.meetingTitle,
                                date = invitation.meetingStartTime.format(DateTimeFormatter.ofPattern("dd MMM")),
                                startTime = invitation.meetingStartTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                endTime = invitation.meetingEndTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                organizerName = invitation.organizerUsername,
                                onAccept = {
                                    viewModel.onEvent(NotificationUiEvent.AcceptInvitation(invitation.id))
                                },
                                onDecline = {
                                    viewModel.onEvent(NotificationUiEvent.DeclineInvitation(invitation.id))
                                },
                                onClick = {
                                    onNavigateToDetails(invitation.meetingId.toString())
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
