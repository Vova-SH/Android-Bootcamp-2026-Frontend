package com.example.frontend.presentation.screens.meetuplist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.data.model.MeetupWithInvitesDTO
import com.example.frontend.presentation.viewmodel.MeetupViewModel
import com.example.frontend.presentation.viewmodel.UiState
import com.example.frontend.presentation.theme.Purple
import com.example.frontend.presentation.theme.BgGray
import com.example.frontend.presentation.theme.Black
import com.example.frontend.presentation.theme.Gray1
import com.example.frontend.presentation.theme.Gray2
import com.example.frontend.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetupListScreen(
    onMeetupClick: (MeetupWithInvitesDTO) -> Unit,
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: MeetupViewModel = viewModel()
) {
    val ms by viewModel.meetupsState.collectAsState()
    
    // загружает митапы при старте
    LaunchedEffect(Unit) {
        viewModel.loadAllPersonsMeetups()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Meetups",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple),
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Purple,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Meetup", tint = Color.White)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Purple,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = onProfileClick,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                )
            }
        }
    ) { pad ->
        Box(modifier = Modifier.fillMaxSize().background(BgGray).padding(pad)) {
            when (ms) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Purple
                    )
                }
                is UiState.Success -> {
                    val suc = ms as UiState.Success<List<MeetupWithInvitesDTO>>
                    val m = suc.data
                    
                    if (m.isEmpty()) {
                        Text(
                            text = "No meetups available",
                            modifier = Modifier.align(Alignment.Center),
                            color = Gray1
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(m) { meetup ->
                                MeetupCard(meetup = meetup, onClick = { onMeetupClick(meetup) })
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    val err = ms as UiState.Error
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error: ${err.message}", color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadAllPersonsMeetups() },
                            colors = ButtonDefaults.buttonColors(containerColor = Purple)
                        ) {
                            Text("Retry")
                        }
                    }
                }
                is UiState.Idle -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetupCard(meetup: MeetupWithInvitesDTO, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Meetup #${meetup.id}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Organizer: ${meetup.planner.name}",
                fontSize = 14.sp,
                color = Gray1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = meetup.date, fontSize = 12.sp, color = Gray2)
                Text(text = meetup.time, fontSize = 12.sp, color = Gray2)
            }
            
            val invs = meetup.invites
            if (invs != null && invs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${invs.size} invited", fontSize = 12.sp, color = Purple)
            }
        }
    }
}
