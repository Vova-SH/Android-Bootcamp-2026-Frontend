package ru.sicampus.bootcamp2026.ui.screens.meetings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ru.sicampus.bootcamp2026.domain.users.entities.MeetingEntity

@Composable
fun MeetingsScreen(
    viewModel: MeetingsViewModel = viewModel<MeetingsViewModel>()
) {
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is MeetingsState.Loading -> MeetingsLoadingState()
        is MeetingsState.Content -> MeetingsContentState(
            currentState,
            onRefresh = {
                viewModel.onIntent(MeetingsIntent.Refresh)
            },
            onLoadMore = {
                viewModel.onIntent(MeetingsIntent.LoadMore)
            }
        )
        is MeetingsState.Error -> MeetingsErrorState(
            currentState,
            onRefresh = {
                viewModel.onIntent(MeetingsIntent.Refresh)
            })
    }
}

@Preview
@Composable
private fun MeetingsLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun MeetingsErrorState(
    state: MeetingsState.Error,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ) {
                Text("Refresh")
            }
        }
    }
}

@Composable
private fun MeetingsContentState(
    state: MeetingsState.Content,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
) {
    val lazyColumnListState = rememberLazyListState()
    val isNeededLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem =
                lazyColumnListState.layoutInfo
                    .visibleItemsInfo
                    .lastOrNull()?.index ?: Int.MIN_VALUE
            val totalItems = lazyColumnListState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 5
        }
    }
    LaunchedEffect(isNeededLoadMore, state.isLastPage) {
        if (isNeededLoadMore && !state.isLastPage) onLoadMore.invoke()
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnListState
    ) {

        items(state.meetings) { item ->
            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 4.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ) ) {
                    when (item) {
                        is MeetingsState.Item.Error -> ItemError(onRefresh)
                        is MeetingsState.Item.Loading -> ItemLoading()
                        is MeetingsState.Item.Meeting -> ItemMeeting(item.meetingEntity)
                    }
            }
        }
    }
}

@Composable
fun ItemMeeting(
    meeting: MeetingEntity
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(meeting.title)
            Text("${meeting.date}")
            Text("${meeting.startTime} ${meeting.endTime}")
            Text("${meeting.creatorId}")
        }
    }
}

@Composable
fun ItemLoading() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun ItemError(
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onRefresh
        ) {
            Text("Try again")
        }
    }
}
