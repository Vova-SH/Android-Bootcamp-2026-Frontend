package ru.sicampus.bootcamp2026.ui.screen.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import ru.sicampus.bootcamp2026.navigation.NavBar
import ru.sicampus.bootcamp2026.navigation.Routes
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.components.SimpleButton
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun ScheduleScreen(
    navController: NavHostController,
    viewModel: ScheduleStateModel = viewModel<ScheduleStateModel>()
) {
    val state by viewModel.uiState.collectAsState()
    
    ScheduleScreenContent(
        state = state,
        navController = navController,
        onRefresh = { viewModel.getData() }
    )
}

@Composable
private fun ScheduleScreenContent(
    state: ScheduleState,
    navController: NavHostController,
    onRefresh: () -> Unit
) {
    when (state) {
        is ScheduleState.Loading -> ScheduleStateLoading()
        is ScheduleState.Error -> ScheduleStateError(state, onRefresh = onRefresh)
        is ScheduleState.Content -> ScheduleStateContent(
            state = state,
            navController = navController
        )
    }
}

@Composable
private fun ScheduleStateContent(
    state: ScheduleState.Content,
    navController: NavHostController
) {
    var selectedTab by remember { mutableStateOf("неделя") }

    Scaffold(
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .systemBarsPadding()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Расписание",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(30.dp))

                    val tabs = listOf("день", "неделя", "месяц")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tabs.forEach { title ->
                            TabItem(title, selectedTab == title) { selectedTab = title }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant, 
                            RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                        )
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (state.meetings.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "У вас пока нет встреч",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        state.meetings.forEach { meeting ->
                            ScheduleCard(
                                title = meeting.title,
                                organizer = meeting.organizerId.toString(), // We might want to show name instead
                                date = meeting.date.toString(),
                                time = "${meeting.startHour}:00"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            IconButton(
                onClick = { 
                    navController.navigate(Routes.Auth.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Выход",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun ScheduleStateError(
    state: ScheduleState.Error,
    onRefresh: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ошибка: ${state.reason}", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            SimpleButton("Повторить") {
                onRefresh()
            }
        }
    }
}

@Composable
private fun ScheduleStateLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun ScheduleCard(title: String, organizer: String, date: String, time: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title, 
                fontSize = 20.sp, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Организатор: $organizer", 
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Дата: $date", 
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "Время: $time", 
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScheduleScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        ScheduleScreenContent(
            state = ScheduleState.Content(),
            navController = rememberNavController(),
            onRefresh = {}
        )
    }
}
