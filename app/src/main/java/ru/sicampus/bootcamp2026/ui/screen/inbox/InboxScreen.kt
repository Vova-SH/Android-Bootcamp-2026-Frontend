package ru.sicampus.bootcamp2026.ui.screen.inbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

@Composable
fun InboxScreen(
    viewmodel: InboxViewModel = viewModel<InboxViewModel>(),
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    when (val state = state) {
        is InboxState.Content ->  InboxContentState(state.invitations)
        is InboxState.Error -> InboxErrorState(
            state.reason
        ) { viewmodel.onIntent(InboxIntent.LoadInvitations) }

        is InboxState.Loading -> InboxLoadingState()
    }
}
@Composable
fun InboxLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}
@Composable
fun InboxErrorState(
    error: String,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Error: $error", color = androidx.compose.ui.graphics.Color.Red)
        Text(text = "Tap to refresh", modifier = Modifier.clickable { onRefresh() })
    }
}
@Composable
fun InboxContentState(
    invitations: List<InvitationEntity>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(invitations) { invitation ->
            Column(modifier = Modifier.fillMaxSize()) {
                Text(invitation.message)
                Text(invitation.status)
                Text(invitation.meeting.name)
                Text(invitation.meeting.description)
                Text(invitation.meeting.startTime)
                Text(invitation.meeting.ownerName)
            }
        }
    }
}