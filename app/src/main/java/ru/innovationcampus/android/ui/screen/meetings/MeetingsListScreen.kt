package ru.innovationcampus.android.ui.screen.meetings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ru.innovationcampus.android.domain.list.entities.UserEntity
import ru.innovationcampus.android.domain.meetings.entities.MeetingEntity
import ru.innovationcampus.android.ui.screen.list.ListIntent
import ru.innovationcampus.android.ui.screen.list.ListState
import ru.innovationcampus.android.ui.screen.list.ListViewModel
import ru.innovationcampus.android.ui.theme.Typography

@Composable
fun MeetingsListScreen(
    viewModel: MeetingsListViewModel = viewModel<MeetingsListViewModel>(),
    navController: NavController,
) {
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is MeetingsListState.Error -> MeetingsListErrorState(
            currentState,
            onRefresh = {
                viewModel.onIntent(MeetingsListIntent.Refresh)
            }
        )

        is MeetingsListState.Loading -> MeetingsListLoadingState()
        is MeetingsListState.Content -> ListContentState(
            currentState,
            onRefresh = {
                viewModel.onIntent(MeetingsListIntent.Refresh)
            },
            onLoadMore = {
                viewModel.onIntent(MeetingsListIntent.LoadMore)
            }
        )
    }
}

@Composable
private fun MeetingsListLoadingState() {
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
private fun MeetingsListErrorState(
    state: MeetingsListState.Error,
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
private fun ListContentState(
    state: MeetingsListState.Content,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val lazyColumnListState = rememberLazyListState()
    val isNeededLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem =
                lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: Int.MIN_VALUE
            val totalItems = lazyColumnListState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 5
        }
    }

    LaunchedEffect(isNeededLoadMore, state.isLastPage) {
        if (isNeededLoadMore && !state.isLastPage) onLoadMore.invoke()
    }
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp),
            state = lazyColumnListState
        ) {
            item {
                Text(
                    "Предстоящие встречи",
                    modifier = Modifier.padding(vertical = 10.dp),
                    style = Typography.headlineLarge
                )
            }
            items(state.meetings) { item ->
                when (item) {
                    is MeetingsListState.Item.Error -> ItemError(onRefresh)
                    is MeetingsListState.Item.Loading -> ItemLoading()
                    is MeetingsListState.Item.Meeting -> ItemMeeting(item.entity)
                }
            }
        }
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
            Text("Load failed. Click me for try again")
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
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ItemMeeting(
    meeting: MeetingEntity
) {
    OutlinedCard(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text("Название: ${meeting.title}", style = Typography.titleLarge)
            Text("Создатель: ${meeting.creatorName}", style = Typography.titleMedium)
            Text("Дата и время: ${meeting.date}", style = Typography.titleMedium)
            Text(
                "Описание: ${meeting.description}", style = Typography.labelLarge,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}