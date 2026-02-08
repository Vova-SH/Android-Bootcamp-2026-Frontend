package com.example.frontend.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.data.auth.SessionManager
import com.example.frontend.data.model.MeetupDTO
import com.example.frontend.data.model.PersonWithInvitesDTO
import com.example.frontend.presentation.theme.Purple
import com.example.frontend.presentation.viewmodel.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val sm = remember { SessionManager(ctx) }
    val s by viewModel.uiState.collectAsState()
    var edit by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.setSessionManager(sm)
        viewModel.loadUserProfile()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Профиль", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple),
                actions = {
                    IconButton(onClick = { edit = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Редактировать", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    selected = false,
                    onClick = onHomeClick,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Purple,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    ) { pad ->
        Box(modifier = Modifier.fillMaxSize().padding(pad)) {
            when {
                s.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                s.errorMsg != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = s.errorMsg ?: "", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadUserProfile() }) {
                            Text("Повторить")
                        }
                    }
                }
                s.userInfo != null -> {
                    val u = s.userInfo
                    if (u != null) {
                        ProfileContent(user = u, meetups = s.organizedMeetups)
                    }
                }
            }
        }
    }
    
    if (edit) {
        EditProfileDialog(
            current = s.userInfo,
            onDismiss = { edit = false },
            onSave = { name, dept ->
                val u = s.userInfo
                if (u != null) {
                    viewModel.updateProfile(u.id, name, dept)
                }
                edit = false
            }
        )
    }
}

@Composable
fun ProfileContent(user: PersonWithInvitesDTO, meetups: List<MeetupDTO>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { UserInfoCard(user = user) }
        
        item {
            Text(
                text = "Мои митапы (организатор)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        if (meetups.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "У вас пока нет организованных митапов",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(meetups) { m -> MeetupCard(meetup = m) }
        }
    }
}

@Composable
fun UserInfoCard(user: PersonWithInvitesDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Логин: ${user.login}", style = MaterialTheme.typography.bodyLarge)
            if (user.dept != null) {
                Text(
                    text = "Отдел: ${user.dept}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MeetupCard(meetup: MeetupDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Дата: ${meetup.date}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(text = "Время: ${meetup.time}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    current: PersonWithInvitesDTO?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var n by remember { mutableStateOf(current?.name ?: "") }
    var d by remember { mutableStateOf(current?.dept ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать профиль") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = n,
                    onValueChange = { n = it },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = d,
                    onValueChange = { d = it },
                    label = { Text("Отдел") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(n, d) }, enabled = n.isNotBlank()) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
