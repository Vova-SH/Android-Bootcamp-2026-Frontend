package ru.sicampus.bootcamp2026.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.ui.components.*
import ru.sicampus.bootcamp2026.ui.screen.profile.ProfileViewModel
import ru.sicampus.bootcamp2026.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val logoutEvent by viewModel.logoutEvent.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.loadProfile() }
    LaunchedEffect(logoutEvent) { if (logoutEvent) onLogout() }

    JuicyBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Профиль", style = MaterialTheme.typography.displayMedium)

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MainGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user?.firstName?.take(1)?.uppercase() ?: "",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "${user?.firstName} ${user?.secondName}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(user?.email ?: "", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
            if (!user?.position.isNullOrBlank()) {
                Text(
                    user?.position ?: "",
                    color = BrandPrimary,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            JuicyCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    SettingItem(
                        text = "Редактировать",
                        icon = Icons.Rounded.Edit,
                        onClick = { showEditDialog = true }
                    )
                    Spacer(Modifier.height(16.dp))
                    Divider(color = OutlineLight)
                    Spacer(Modifier.height(16.dp))
                    SettingItem(
                        text = "Выйти",
                        icon = Icons.Rounded.ExitToApp,
                        isDestructive = true,
                        onClick = { viewModel.logout() }
                    )
                }
            }
        }

        if (showEditDialog && user != null) {
            EditProfileDialog(
                user = user!!,
                onDismiss = { showEditDialog = false },
                onConfirm = { fName, sName, pos ->
                    viewModel.updateProfile(fName, sName, pos)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    user: UserDto,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf(user.firstName) }
    var secondName by remember { mutableStateOf(user.secondName) }
    var position by remember { mutableStateOf(user.position ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактирование") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                JuicyTextField(value = firstName, onValueChange = { firstName = it }, label = "Имя")
                JuicyTextField(value = secondName, onValueChange = { secondName = it }, label = "Фамилия")
                JuicyTextField(value = position, onValueChange = { position = it }, label = "Должность")
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(firstName, secondName, position) },
                enabled = firstName.isNotBlank() && secondName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        containerColor = SurfaceWhite
    )
}

@Composable
fun SettingItem(text: String, icon: ImageVector, isDestructive: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (isDestructive) Color(0xFFFEF2F2) else SurfaceLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                null,
                tint = if (isDestructive) BrandTertiary else BrandPrimary
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text,
            fontWeight = FontWeight.SemiBold,
            color = if (isDestructive) BrandTertiary else TextPrimary
        )
    }
}