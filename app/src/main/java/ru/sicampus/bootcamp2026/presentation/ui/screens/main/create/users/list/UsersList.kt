package ru.sicampus.bootcamp2026.presentation.ui.screens.main.create.users.list

import android.renderscript.Allocation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.presentation.ui.theme.AppTypography
// todo add reloading by swap vertically

@Composable
fun UsersList(
    viewModel: MyListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    when(val currentState = state) {
        is ListState.Loading -> ListLoadingState()
        is ListState.Content -> ListContentState(currentState)
        is ListState.Error -> ListErrorState(currentState, onRefresh = {viewModel.getData()})
    }

}
@Composable
fun UserElement() {

}

@Composable
fun ListLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun ListErrorState(
    state: ListState.Error,
    onRefresh: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason, style = AppTypography.titleMedium)
            Button(onClick = onRefresh) {
                Text("Refresh")
            }
        }
    }
}

@Composable
fun ListContentState(
    state: ListState.Content
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        // todo
    }
}