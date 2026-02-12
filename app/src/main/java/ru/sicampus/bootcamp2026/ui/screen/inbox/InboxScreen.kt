package ru.sicampus.bootcamp2026.ui.screen.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.components.InboxItem
import ru.sicampus.bootcamp2026.components.Title
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun InboxScreen(
    viewmodel: InboxViewModel = viewModel<InboxViewModel>(),
) {
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    when (val state = state) {
        is InboxState.Content ->  InboxContentState(
            state.invitations,
            onClick = { intent -> viewmodel.onIntent(intent) }
        )
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
        Text(text = "Error: $error", color = Color.Red)
        Text(text = "Tap to refresh", modifier = Modifier.clickable { onRefresh() })
    }
}
@Composable
fun InboxContentState(
    invitations: List<InvitationEntity>,
    onClick: (InboxIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        Title("Входящие")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                Spacer(modifier = Modifier.height(58.dp))
            }

            items(invitations) { invitation ->
                InboxItem(
                    invitation,
                    onAccept = {onClick(InboxIntent.AcceptInvitation(invitation.id)) },
                    onDecline = {onClick(InboxIntent.DeclineInvitation(invitation.id)) }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Spacer(modifier = Modifier.height(128.dp))
            }
        }
    }
}