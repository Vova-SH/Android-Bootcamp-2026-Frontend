package ru.sicampus.bootcamp2026.ui.theme.screen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

@Composable
fun ListScreen(
    viewModel: ListViewModel = viewModel<ListViewModel>(),
    navController: NavController,
) {
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is ListState.Error -> ListErrorState(
            currentState,
            onRefresh = {
                viewModel.onIntent(ListIntent.Refresh)
            }
        )
        is ListState.Loading -> ListLoadingState()
        is ListState.Content -> ListContentState(
            currentState,
            onRefresh = {
                viewModel.onIntent(ListIntent.Refresh)
            },
            onLoadMore = {
                viewModel.onIntent(ListIntent.LoadMore)
            }
        )
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
private fun ListContentState(
    state: ListState.Content,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val lazyColumnListState = rememberLazyListState()
    val isNeededLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: Int.MIN_VALUE
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
                is ListState.Item.Error -> ItemError(onRefresh)
                is ListState.Item.Loading -> ItemLoading()
                is ListState.Item.User -> ItemUser(item.entity)
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
fun ItemUser(
    user: UserEntity
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(user.name)
            Text(user.email)
        }
    }
}
