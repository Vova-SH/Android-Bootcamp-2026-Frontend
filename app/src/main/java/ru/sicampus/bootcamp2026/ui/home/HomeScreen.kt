package ru.sicampus.bootcamp2026.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.components.HomeMeetingCard
import ru.sicampus.bootcamp2026.ui.components.HomeFilterDialog
import java.time.format.DateTimeFormatter

val GreenLight = Color(0xFFBBDBA6)

@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Локальное состояние UI
    var showFilterDialog by remember { mutableStateOf(false) }
    var isSortExpanded by remember { mutableStateOf(false) }

    // Получаем состояние из ViewModel
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val sidePadding = 24.dp
    val dateFormatter = DateTimeFormatter.ofPattern("dd, EEE")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Box контейнер
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Хедер с приветствием и профилем
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good morning,",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                    Text(
                        text = state.username.ifEmpty { "User" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = GreenLight
                    )
                }
                IconButton(
                    onClick = onNavigateToProfile,
                    modifier = Modifier.size(56.dp)
                ) {
                    Surface(shape = CircleShape, color = Color.Gray) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.padding(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка создания новой встречи
            Button(
                onClick = onNavigateToCreate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding)
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Create new meeting",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Секция приглашений
            Text(
                text = "My invitations",
                color = GreenLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = sidePadding),
                fontSize = 24.sp,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Панель фильтров и сортировки
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding)
                    .height(64.dp),
                color = Color(0xFF2A2A2A).copy(alpha = 0.6f),
                shape = RoundedCornerShape(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кнопка Фильтра
                    FilledIconButton(
                        onClick = { showFilterDialog = true },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = GreenLight),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "Filter",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Выпадающий список сортировки
                    Box {
                        Button(
                            onClick = { isSortExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = if (state.sortOrder == SortOrder.DECREASING) "Decreasing" else "Increasing",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowDropDown, null, tint = Color.Black)
                        }

                        DropdownMenu(
                            expanded = isSortExpanded,
                            onDismissRequest = { isSortExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Increasing") },
                                onClick = {
                                    viewModel.onEvent(HomeUiEvent.ChangeSortOrder(SortOrder.INCREASING))
                                    isSortExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Decreasing") },
                                onClick = {
                                    viewModel.onEvent(HomeUiEvent.ChangeSortOrder(SortOrder.DECREASING))
                                    isSortExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Диалог фильтров
            if (showFilterDialog) {
                HomeFilterDialog(
                    onDismiss = { showFilterDialog = false },
                    onApply = { status: String? ->
                        viewModel.onEvent(HomeUiEvent.FilterByStatus(status))
                        showFilterDialog = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список встреч с состояниями загрузки и ошибок
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenLight)
                    }
                }
                state.error != null -> {
                    // Определяем тип ошибки
                    val isConnectionError = state.error!!.contains("Connection", ignoreCase = true) ||
                            state.error!!.contains("timeout", ignoreCase = true) ||
                            state.error!!.contains("unreachable", ignoreCase = true) ||
                            state.error!!.contains("failed to connect", ignoreCase = true) ||
                            state.error!!.contains("NetworkException", ignoreCase = true)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isConnectionError) "the server is not responding" else "Error loading meetings",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!isConnectionError) {
                            Text(
                                text = state.error!!,
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        Button(
                            onClick = { viewModel.onEvent(HomeUiEvent.LoadMeetings) },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenLight)
                        ) {
                            Text("Retry", color = Color.Black)
                        }
                    }
                }
                state.filteredMeetings.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No meetings found",
                            color = Color.White.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.filteredMeetings) { meeting ->
                            Box(modifier = Modifier.padding(horizontal = sidePadding)) {
                                HomeMeetingCard(
                                    title = meeting.title,
                                    date = meeting.startTime.format(dateFormatter),
                                    startTime = meeting.startTime.format(timeFormatter),
                                    endTime = meeting.endTime.format(timeFormatter),
                                    participantsCount = meeting.participants.size,
                                    onClick = { onNavigateToDetails(meeting.id.toString()) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToCreate = {},
        onNavigateToDetails = {},
        onNavigateToProfile = {}
    )
}