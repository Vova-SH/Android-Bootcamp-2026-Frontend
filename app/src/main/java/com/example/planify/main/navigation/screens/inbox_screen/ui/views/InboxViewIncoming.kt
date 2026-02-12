package com.example.planify.main.navigation.screens.inbox_screen.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.navigation.screens.fixed_screens.ErrorScreen
import com.example.planify.main.navigation.screens.inbox_screen.InboxAction
import com.example.planify.main.navigation.screens.inbox_screen.InboxViewModel
import com.example.planify.main.navigation.screens.inbox_screen.ui.components.MeetingInviteInboxCard

@Composable
fun InboxViewIncoming(
    viewModel: InboxViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(
                state = rememberScrollState()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Locals.dimens.inboxBoxTopNavBarHeight,
                    bottom = Locals.spacing.s
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Locals.spacing.m)
            ) {
                uiState.actions.values.forEach {
                    when(it) {
                        is ResourceState.Loading -> {} // TODO: Skeletons
                        is ResourceState.Error -> {}
                        is ResourceState.Idle -> {}
                        is ResourceState.Success -> {
                            when(it.data) {
                                is InboxAction.Invite -> {
                                    val context = it.data.meetingContext
                                    MeetingInviteInboxCard(
                                        meetingInfo = context,
                                        onAccept = { viewModel.acceptMeeting(it.data.inviteUuid, it.data.actionId) },
                                        onReject = { viewModel.rejectMeeting(it.data.inviteUuid, it.data.actionId) }
                                    )

                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}