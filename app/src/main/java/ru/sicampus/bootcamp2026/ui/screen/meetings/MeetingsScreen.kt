package ru.sicampus.bootcamp2026.ui.screen.meetings.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.FlowPreview
import ru.sicampus.bootcamp2026.domain.entities.EmployeeEntity
import ru.sicampus.bootcamp2026.ui.screen.meetings.EmployeeListIntent
import ru.sicampus.bootcamp2026.ui.screen.meetings.EmployeeSearchState
import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingViewModel

@OptIn(FlowPreview::class)
@Composable

fun MeetingsScreen(
    viewmodel: MeetingViewModel = viewModel<MeetingViewModel>(),
) {
    val state by viewmodel.uiState.collectAsState()
    var selectedUserName by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        viewmodel.onIntent(EmployeeListIntent.Refresh)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        SearchField(viewmodel.searchState)

        Box(modifier = Modifier.weight(1f)) {
            when (val currentState = state) {
                is EmployeeSearchState.Error -> ListErrorState(
                    state = currentState,
                    onRefresh = { viewmodel.onIntent(EmployeeListIntent.Refresh) }
                )
                is EmployeeSearchState.Loading -> ListLoadingState()
                is EmployeeSearchState.Content -> ListContentState(
                    currentState,
                    selectedUserName,
                    onUserSelected = { name ->
                        selectedUserName = if (selectedUserName == name) null else name
                    },
                    onRefresh = { viewmodel.onIntent(EmployeeListIntent.Refresh)},
                    onLoadMore = {viewmodel.onIntent(EmployeeListIntent.LoadMore)}
                )
            }
        }
    }
}
@Composable
private fun ListLoadingState(
){
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
private fun ListErrorState(
    state: EmployeeSearchState.Error,
    onRefresh: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ) {
                Text("Refresh")
            }
        }
    }
}
@OptIn(FlowPreview::class)
@Composable
private fun ListContentState(
    state: EmployeeSearchState.Content,
    selectedUserName: String?,
    onUserSelected: (String) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
){
    val lazyColumnListState = rememberLazyListState()
    val isNeededLoadMore by remember{
        derivedStateOf {
            val lastVisibleItem = lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: Int.MIN_VALUE
            val totalItems = lazyColumnListState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 20
        }
    }

    LaunchedEffect(isNeededLoadMore, state.isLastPage) {
        if(isNeededLoadMore && !state.isLastPage) onLoadMore.invoke()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = lazyColumnListState
    ) {
        item { Box(contentAlignment = Alignment.Center) {
                Text(
                    text = state.inputError ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Red
                )
            }
        }
        if (state.users.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Пользователей не обнаружено",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(state.users){ item ->
                when(item) {
                    is EmployeeSearchState.Item.Employee -> ItemUser(
                        item.entity,
                        selectedUserName,
                        onClick = { onUserSelected(item.entity.name) }
                    )
                    is EmployeeSearchState.Item.Error -> ItemError(onRefresh)
                    is EmployeeSearchState.Item.Loading -> ItemLoading()
                }

            }
        }
    }
}
@Composable
private fun SearchField(
    searchState: TextFieldState
){
    OutlinedTextField(
        state = searchState,
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}
@Composable
fun ItemError(
    onRefresh: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onRefresh) {
            Text("Load Failed. Click to try again")
        }
    }
}
@Composable
fun ItemLoading(){
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp)
        )
    }
}
@Composable
fun ItemUser(
    user: EmployeeEntity,
    selectedUserName: String? = null,
    onClick: () -> Unit
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape),
            model = user.photoUrl,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(user.name)
            Text(user.position)

            if (selectedUserName == user.name) {
                Text(user.username)
                Text(user.email)
                Text(user.phoneNumber)
            }
        }
    }
}