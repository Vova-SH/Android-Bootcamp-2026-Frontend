package ru.sicampus.bootcamp2026.ui.screen.inviting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.navigation.NavBar
import ru.sicampus.bootcamp2026.navigation.Routes
import ru.sicampus.bootcamp2026.components.SimpleButton

import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun InvitingScreen(
    navController: NavHostController,
    viewModel: InvitingStateModel = viewModel<InvitingStateModel>()
) {
    val state by viewModel.uiState.collectAsState()
    
    InvitingScreenContent(
        state = state,
        navController = navController,
        onRefresh = { viewModel.getData() },
        onRespond = { invitation, status -> viewModel.respondToInvitation(invitation, status) }
    )
}

@Composable
private fun InvitingScreenContent(
    state: InvitingState,
    navController: NavHostController,
    onRefresh: () -> Unit,
    onRespond: (InvitationEntity, Int) -> Unit
) {
    when (state) {
        is InvitingState.Loading -> InvitingStateLoading()
        is InvitingState.Error -> InvitingStateError(state, onRefresh = onRefresh)
        is InvitingState.Content -> InvitingStateContent(
            state = state,
            navController = navController,
            onRespond = onRespond
        )
    }
}

@Composable
private fun InvitingStateContent(
    state: InvitingState.Content,
    navController: NavHostController,
    onRespond: (InvitationEntity, Int) -> Unit
) {
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
                        text = "Новые приглашения",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    
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
                    if (state.invitations.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "У вас пока нет приглашений",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        state.invitations.filter { it.status == 0 }.forEach { invitation ->
                            InvitationCard(
                                title = invitation.meeting?.title ?: "Без названия",
                                organizer = invitation.meeting?.organizerId?.toString() ?: "Неизвестно",
                                date = invitation.meeting?.date?.toString() ?: "??.??.????",
                                time = "${invitation.meeting?.startHour ?: 0}:00",
                                onAccept = { onRespond(invitation, 1) },
                                onReject = { onRespond(invitation, 2) }
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
private fun InvitingStateError(
    state: InvitingState.Error,
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
private fun InvitingStateLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun InvitationCard(
    title: String,
    organizer: String,
    date: String,
    time: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title, 
                fontSize = 20.sp, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Column(modifier = Modifier.fillMaxWidth()) {
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SimpleButton("Принять",
                    modifier = Modifier.weight(1f)
                ) { onAccept() }
                
                SimpleButton("Отклонить",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.error
                ) { onReject() }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun InvitingScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        InvitingScreenContent(
            state = InvitingState.Content(),
            navController = rememberNavController(),
            onRefresh = {},
            onRespond = { _, _ -> }
        )
    }
}
