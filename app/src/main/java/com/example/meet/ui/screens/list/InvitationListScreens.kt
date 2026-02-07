package com.example.meet.ui.screens.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet.R
import kotlinx.coroutines.launch
import com.example.meet.data.dto.InvitationDto
import com.example.meet.data.dto.InvitationResponseStatus
import com.example.meet.data.source.DataLocator
import kotlinx.serialization.ExperimentalSerializationApi

sealed class InvitationsUiState {
    data object Loading : InvitationsUiState()
    data class Success(val invitations: List<InvitationDto>) : InvitationsUiState()
    data class Error(val message: String) : InvitationsUiState()
}

@ExperimentalSerializationApi
@ExperimentalMaterial3Api
@Composable
fun InvitationsListScreen(navController: NavHostController) {
    val ds = remember { DataLocator.userInfoDataSource }
    val invDs = remember { DataLocator.invitationDataSource }
    val scope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf<InvitationsUiState>(InvitationsUiState.Loading) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val invitations = ds.loadActiveInvitations()
                uiState = InvitationsUiState.Success(invitations)
            } catch (e: Exception) {
                uiState = InvitationsUiState.Error("Не удалось загрузить приглашения: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Приглашения") },
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
                InvitationsUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                is InvitationsUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                is InvitationsUiState.Success -> {
                    if (state.invitations.isEmpty()) {
                        Text(
                            text = "Активных приглашений нет",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.invitations) { invitation ->
                                InvitationCard(
                                    invitation = invitation,
                                    onRespond = { status ->
                                        scope.launch {
                                            try {
                                                val updated = invDs.updateInvitationResponse(
                                                    invitationId = invitation.id.toInt(),
                                                    responseStatus = status,
                                                    comment = null
                                                ).getOrThrow()
                                                val newList = state.invitations
                                                    .map { if (it.id == invitation.id) updated else it }
                                                    .filter { it.responseStatus.equals(InvitationResponseStatus.PENDING, ignoreCase = true) }
                                                uiState = InvitationsUiState.Success(newList)
                                            } catch (e: Exception) {
                                                uiState = InvitationsUiState.Error("Ошибка: ${e.message}")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InvitationCard(
    invitation: InvitationDto,
    onRespond: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Встреча #${invitation.meetingId}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Обязательное участие: ${if (invitation.isRequired) "да" else "нет"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Статус ответа: ${invitation.responseStatus}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onRespond(InvitationResponseStatus.ACCEPTED) }) {
                    Text("Принять")
                }
                OutlinedButton(onClick = { onRespond(InvitationResponseStatus.DECLINED) }) {
                    Text("Отклонить")
                }
            }
        }
    }
}
