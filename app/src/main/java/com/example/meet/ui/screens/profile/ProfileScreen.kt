package com.example.meet.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet.R
import kotlinx.coroutines.launch
import com.example.meet.Screen
import com.example.meet.data.dto.MeetingDto
import com.example.meet.data.dto.UserDto
import com.example.meet.data.source.DataLocator
import com.example.meet.data.source.Network
import kotlinx.serialization.ExperimentalSerializationApi

data class ProfileData(
    val user: UserDto,
    val meetings: List<MeetingDto>
)

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val data: ProfileData) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@ExperimentalSerializationApi
@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(navController: NavHostController) {
    val ds = remember { DataLocator.userInfoDataSource }
    val scope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf<ProfileUiState>(ProfileUiState.Loading) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val user = ds.loadCurrentUser()
                val meetings = ds.loadMeetingsForCurrentUser()
                uiState = ProfileUiState.Success(ProfileData(user, meetings))
            } catch (e: Exception) {
                uiState = ProfileUiState.Error("Не удалось загрузить профиль: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("main_meet") {
                                    popUpTo("main_meet") { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Назад",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.InfoProfile.route) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_visibility),
                            contentDescription = "Подробнее",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            Network.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_exit_to_app),
                            contentDescription = "Выйти",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                ProfileUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                is ProfileUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                is ProfileUiState.Success -> {
                    ProfileContent(data = state.data)
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(data: ProfileData) {
    val user = data.user
    val meetings = data.meetings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = user.fullName, style = MaterialTheme.typography.titleLarge)
        Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        if (!user.position.isNullOrBlank()) {
            Text(text = "Должность: ${user.position}", style = MaterialTheme.typography.bodyMedium)
        }
        if (!user.department.isNullOrBlank()) {
            Text(text = "Отдел: ${user.department}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Статистика встреч",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Всего встреч: ${meetings.size}",
            style = MaterialTheme.typography.bodyMedium
        )

        val upcoming = meetings.count { it.status == "PLANNED" || it.status == "SCHEDULED" }
        val finished = meetings.count { it.status == "COMPLETED" }

        Text(text = "Запланировано: $upcoming", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Завершено: $finished", style = MaterialTheme.typography.bodyMedium)
    }
}