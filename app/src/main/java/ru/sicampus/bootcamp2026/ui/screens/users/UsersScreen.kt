package ru.sicampus.bootcamp2026.ui.screens.users

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import ru.sicampus.bootcamp2026.domain.users.entities.UserEntity

@Composable
fun UsersScreen(
    viewModel: UsersViewModel = viewModel<UsersViewModel>()
) {
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is UsersState.Loading -> UsersLoadingState()
        is UsersState.Content -> UsersContentState(
            currentState,
            onRefresh = {
                viewModel.onIntent(UsersIntent.Refresh)
            },
            onLoadMore = {
                viewModel.onIntent(UsersIntent.LoadMore)
            }
        )
        is UsersState.Error -> UsersErrorState(
            currentState,
            onRefresh = {
                viewModel.onIntent(UsersIntent.Refresh)
            })
    }
}

@Preview
@Composable
private fun UsersLoadingState() {
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
private fun UsersErrorState(
    state: UsersState.Error,
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
private fun UsersContentState(
    state: UsersState.Content,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnListState
    ) {
        items(state.users) { item ->
            when (item) {
                is UsersState.Item.Error -> ItemError(onRefresh)
                is UsersState.Item.Loading -> ItemLoading()
                is UsersState.Item.User -> ItemUser(item.userEntity)
            }
        }
    }
}

@Composable
fun ItemUser(
    user: UserEntity
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(48.dp).clip(CircleShape),
            model = user.photoUrl,
            contentDescription = null
        )
        Column {
            Text("${user.surname} ${user.name} ${user.patronymic}")
            Text(user.departmentName)
            Text(user.email)
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

//@Preview
//@Composable
//private fun TestUsersContentState() {
//    UsersContentState(UsersState.Content(
//        listOf<UserEntity>(
//            UserEntity(
//                "Ramis Girfanov", "girfanov2007@gmail.com", "something",
//                surname = TODO(),
//                patronymic = TODO(),
//                username = TODO(),
//                messengerLink = TODO(),
//                phoneNumber = TODO(),
//                departmentName = TODO()
//            ),
//            UserEntity("Somebody Once Told", "somebody@yandex.ru", "metheworldisgonnarollme")
//        )
//    ))
//}
