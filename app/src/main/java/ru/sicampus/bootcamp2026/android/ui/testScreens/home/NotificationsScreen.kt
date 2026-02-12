package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.ui.components.MeetingCard
import ru.sicampus.bootcamp2026.android.ui.components.MeetingCardActions
import ru.sicampus.bootcamp2026.android.ui.theme.Black
import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
import ru.sicampus.bootcamp2026.android.ui.theme.White
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.snapshotFlow
import ru.sicampus.bootcamp2026.android.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.android.ui.utils.toMeetingUi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onExitClick: () -> Unit = {},
    viewModel: NotificationsViewModel
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val myUserId by produceState<Long?>(initialValue = null) {
        value = AuthLocalDataSource.getUserId()
    }

    LaunchedEffect(Unit) { viewModel.loadFirstPage() }

    // пагинация
    LaunchedEffect(listState, state.isLast) {
        snapshotFlow {
            val layout = listState.layoutInfo
            val total = layout.totalItemsCount
            val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: 0

            total > 0 &&
                    state.invites.isNotEmpty() &&
                    !state.isLast &&
                    !state.isLoading &&
                    !state.isLoadingMore &&
                    lastVisible >= total - 2
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { viewModel.loadNextPage() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Уведомления",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.open_sans_bold)),
                        modifier = Modifier.padding(start = 45.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onExitClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = "Закрыть",
                            tint = Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = Black,
                    navigationIconContentColor = Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 30.dp)
            )
        }
    ) { paddingValues ->

        if (!state.isLoading && state.invites.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(176.dp))
                Icon(
                    painter = painterResource(R.drawable.no_notifications_icon),
                    contentDescription = "Нет уведомлений",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "У вас пока нет уведомлений",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = TextGrey
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(White),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = state.invites,
                    key = { it.id }
                ) { invite ->
                    val meeting = invite.meeting

                    val organizerName =
                        state.organizerNames[meeting.organizerId]
                            ?: "Организатор #${meeting.organizerId}"

                    val ui = meeting.toMeetingUi(organizerName = organizerName, myUserId = myUserId)

                    MeetingCard(
                        meeting = ui,
                        actions = MeetingCardActions.InviteActions(
                            onAccept = { viewModel.respond(invite.id, InvitationStatusDto.ACCEPTED) },
                            onDecline = { viewModel.respond(invite.id, InvitationStatusDto.DECLINED) }
                        )
                    )
                }

                if (state.isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
