package ru.sicampus.bootcamp2026.ui.screen.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.ui.components.JuicyBackground
import ru.sicampus.bootcamp2026.ui.components.JuicyCard
import ru.sicampus.bootcamp2026.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    userId: Long,
    viewModel: UserDetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    JuicyBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, "Назад", tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (val state = uiState) {
                    is UserDetailsUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = BrandPrimary)
                    is UserDetailsUiState.Error -> Text(
                        text = state.message,
                        color = BrandTertiary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    is UserDetailsUiState.Content -> UserInfoContent(state.user)
                }
            }
        }
    }
}

@Composable
private fun UserInfoContent(user: UserDto) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(SecondaryGradient, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.firstName.take(1).uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
        }
        Spacer(Modifier.height(24.dp))

        Text(
            text = "${user.firstName} ${user.secondName}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        if (!user.position.isNullOrBlank()) {
            Text(
                text = user.position,
                style = MaterialTheme.typography.titleMedium,
                color = BrandPrimary
            )
        }

        Spacer(Modifier.height(32.dp))

        JuicyCard(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                DetailRow(Icons.Default.Email, "Email", user.email)
                if (!user.patronymic.isNullOrBlank()) {
                    DetailRow(Icons.Default.Person, "Отчество", user.patronymic)
                }
                DetailRow(Icons.Default.Star, "ID", user.id.toString())
                if (user.createdAt != null) {
                    DetailRow(Icons.Default.Star, "Регистрация", user.createdAt.take(10))
                }
            }
        }
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextTertiary)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(value, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        }
    }
}