package ru.sicampus.bootcamp2026.ui.screen.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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

@Composable
fun ListScreen(
    viewModel : ListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is ListState.Content -> ListContentState(currentState)
        is ListState.Error -> ListErrorState(currentState) { viewModel.getData() }
        is ListState.Loading -> ListLoadingState()
    }
}

@Composable
private fun ListLoadingState() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun ListErrorState(
    state: ListState.Error,
    onRefresh: () -> Unit
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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
fun ListContentState(
    state: ListState.Content,
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            state.users.forEach { user ->
                Row {
                    // TODO: IMAGE
                    Column {
                        Text(user.name) // name
                    }
                }
            }
        }
    }
}