package ru.sicampus.bootcamp2026.network.ui.screen.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun ListScreen(
    viewModel: ListViewModel = viewModel<ListViewModel>()
) {
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is ListState.Error -> ListErrorState(currentState, onRefresh = { viewModel.getData() })
        is ListState.Loading -> ListLoadingState()
        is ListState.UserContent -> ListUserContentState(currentState)
        is ListState.MeetingContent -> ListMeetingContentState(currentState)
    }
}

@Composable
private fun ListLoadingState() {
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
private fun ListErrorState(
    state: ListState.Error,
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
private fun ListUserContentState(
    state: ListState.UserContent,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        state.users.forEach { user ->
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                    model = user.photoUrl,
                    contentDescription = null,
                )
                Column {
                    Text(user.name)
                    Text(user.email)
                }
            }
        }
    }
}

@Composable
private fun ListMeetingContentState(
    state: ListState.MeetingContent,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        state.meetings.forEach { meeting ->
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*
                    В ПУЛЛЕ УДАЛИТЬ!!!

                     Честно, я не понимаю что мне тут еще писать,
                     для моей реализации встреч тут очень много применений, возможно, это огромный костыль,
                     возможно, я криворукий и не надо было все пихать в одно, но по другому никак,
                     нам ничего больше то и не нужно, только POST, да и то у него будет
                     примерно такая же структура, но я правда не знаю как его реализовать и надо ли
                     это нам сейчас

                     В ПУЛЛЕ УДАЛИТЬ!!!
                */
            }
        }
    }
}