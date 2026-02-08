package ru.sicampus.bootcamp2026.ui.screen.meetings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.NavRoutes
import ru.sicampus.bootcamp2026.currentUser
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: MeetingsViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is MeetingsState.Content -> MeetingsContentState(
            meetings = currentState.meetings,
            navController = navController,
            viewModel = viewModel,
            modifier = modifier
        )
        is MeetingsState.Error -> MeetingsErrorState(
            state = currentState,
            modifier = modifier
        )
        is MeetingsState.Loading -> MeetingsLoadingState(modifier = modifier)
    }
}

@Composable
fun MeetingsContentState(
    meetings: List<MeetingEntity>,
    navController: NavHostController,
    viewModel: MeetingsViewModel,
    modifier: Modifier
) {
    currentUser?.let { currentUser ->
        var showForMe by remember { mutableStateOf(true) }
        Column (modifier) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = showForMe,
                    onClick = {
                        showForMe = !showForMe
                    },
                    label = { Text("Вам") },
                    trailingIcon = if (showForMe) {
                        {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilterChip(
                    selected = !showForMe,
                    onClick = {
                        showForMe = !showForMe
                        if (!showForMe) showForMe = false
                    },
                    label = { Text("От вас") },
                    trailingIcon = if (showForMe) {
                        {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )

                LazyColumn {
                    if (showForMe) viewModel.getDataForMe()
                    else viewModel.getDataOutMe()
                    items(meetings) {
                        it.ClassicCard(currentUser)
                    }
                }
            }

            Row(Modifier.fillMaxWidth()) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavRoutes.CreateMeeting.route)
                    },
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Создать встречу"
                    )
                }
            }
        }
    }
}

/**
 * Состояние "нет данных" для экрана встреч
 */
@Composable
fun MeetingsEmptyState(
    periodText: String = "",
    showForMe: Boolean = true,
    showFromMe: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when {
                    periodText.isNotEmpty() -> "В $periodText нет встреч"
                    showForMe -> "Вам нет встреч"
                    showFromMe -> "От вас нет встреч"
                    else -> "Нет встреч"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (periodText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = periodText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Состояния загрузки для экрана встреч
 */
@Composable
fun MeetingsLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

/**
 * Состояние ошибки для экрана встреч
 */
@Composable
fun MeetingsErrorState(
    state: MeetingsState.Error,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.reason,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = state.onClickButton
            ) {
                Text(state.buttonText)
            }
        }
    }
}
