package ru.sicampus.bootcamp2026.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sicampus.bootcamp2026.ui.theme.LightGreen
import androidx.compose.foundation.Image

/**
 * Экран профиля пользователя
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Навигация на экран логина после logout
    LaunchedEffect(state.shouldNavigateToLogin) {
        if (state.shouldNavigateToLogin) {
            onNavigateToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            if (!state.isEditMode && !state.isChangingPassword) {
                Row {
                    IconButton(
                        onClick = { viewModel.onEvent(ProfileUiEvent.EnableEditMode) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = LightGreen
                        )
                    }
                    IconButton(
                        onClick = { showLogoutDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        if (state.isLoading && state.username.isEmpty()) {
            // Начальная загрузка
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = LightGreen)
            }
        } else if (state.isChangingPassword) {
            // Экран смены пароля
            ChangePasswordContent(state, viewModel)
        } else {
            // Основной контент профиля
            ProfileContent(state, viewModel)
        }
    }

    // Success dialog
    if (state.successMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileUiEvent.DismissSuccess) },
            title = { Text("Success") },
            text = { Text(state.successMessage!!) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(ProfileUiEvent.DismissSuccess) }
                ) {
                    Text("OK", color = LightGreen)
                }
            }
        )
    }

    // Error dialog
    if (state.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileUiEvent.DismissError) },
            title = { Text("Error") },
            text = { Text(state.error!!) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(ProfileUiEvent.DismissError) }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.onEvent(ProfileUiEvent.Logout)
                    }
                ) {
                    Text("Logout", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    viewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(LightGreen.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            if (state.avatarLoadedBitmap != null) {
                // Показываем загруженное изображение
                Image(
                    bitmap = state.avatarLoadedBitmap.asImageBitmap(),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Показываем иконку по умолчанию
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = LightGreen,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isEditMode) {
            // Режим редактирования
            EditProfileFields(state, viewModel)
        } else {
            // Режим просмотра
            ViewProfileFields(state, viewModel)
        }
    }
}

@Composable
private fun ViewProfileFields(
    state: ProfileUiState,
    viewModel: ProfileViewModel
) {
    // Username
    ProfileInfoCard(
        icon = Icons.Default.Person,
        label = "Username",
        value = state.username
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Email
    ProfileInfoCard(
        icon = Icons.Default.Email,
        label = "Email",
        value = state.email
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Change Password Button
    Button(
        onClick = { viewModel.onEvent(ProfileUiEvent.OpenPasswordChange) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = LightGreen
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Change Password",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EditProfileFields(
    state: ProfileUiState,
    viewModel: ProfileViewModel
) {
    // Username field
    OutlinedTextField(
        value = state.editUsername,
        onValueChange = { viewModel.onEvent(ProfileUiEvent.UsernameChanged(it)) },
        label = { Text("Username *", color = Color.White.copy(alpha = 0.7f)) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = LightGreen,
            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            cursorColor = LightGreen,
            errorBorderColor = Color.Red
        ),
        isError = state.usernameError != null,
        supportingText = state.usernameError?.let { { Text(it, color = Color.Red) } },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = LightGreen
            )
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Avatar URL field
    OutlinedTextField(
        value = state.editAvatarUrl,
        onValueChange = { viewModel.onEvent(ProfileUiEvent.AvatarUrlChanged(it)) },
        label = { Text("Avatar URL (optional)", color = Color.White.copy(alpha = 0.7f)) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = if (state.avatarLoadingError != null) Color.Red else LightGreen,
            unfocusedBorderColor = if (state.avatarLoadingError != null) Color.Red else Color.White.copy(alpha = 0.5f),
            cursorColor = LightGreen,
            errorBorderColor = Color.Red
        ),
        isError = state.avatarLoadingError != null,
        supportingText = state.avatarLoadingError?.let { { Text(it, color = Color.Red, maxLines = 2) } },
        singleLine = true,
        leadingIcon = {
            if (state.avatarLoadedBitmap != null) {
                // Показываем зеленую галочку если изображение успешно загружено
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Image loaded",
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = LightGreen
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Email (read-only)
    OutlinedTextField(
        value = state.email,
        onValueChange = {},
        label = { Text("Email", color = Color.White.copy(alpha = 0.7f)) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.White.copy(alpha = 0.7f),
            disabledBorderColor = Color.White.copy(alpha = 0.3f),
            disabledLabelColor = Color.White.copy(alpha = 0.5f)
        ),
        enabled = false,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f)
            )
        }
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Save/Cancel buttons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { viewModel.onEvent(ProfileUiEvent.CancelEdit) },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
        ) {
            Text("Cancel", fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = { viewModel.onEvent(ProfileUiEvent.SaveProfile) },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Black
                )
            } else {
                Text(
                    text = "Save",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ChangePasswordContent(
    state: ProfileUiState,
    viewModel: ProfileViewModel
) {
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Change Password",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Current Password
        OutlinedTextField(
            value = state.currentPassword,
            onValueChange = { viewModel.onEvent(ProfileUiEvent.CurrentPasswordChanged(it)) },
            label = { Text("Current Password *", color = Color.White.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = LightGreen,
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                cursorColor = LightGreen,
                errorBorderColor = Color.Red
            ),
            visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = state.currentPasswordError != null,
            supportingText = state.currentPasswordError?.let { { Text(it, color = Color.Red) } },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LightGreen
                )
            },
            trailingIcon = {
                IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                    Icon(
                        imageVector = if (showCurrentPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (showCurrentPassword) "Hide password" else "Show password",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New Password
        OutlinedTextField(
            value = state.newPassword,
            onValueChange = { viewModel.onEvent(ProfileUiEvent.NewPasswordChanged(it)) },
            label = { Text("New Password *", color = Color.White.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = LightGreen,
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                cursorColor = LightGreen,
                errorBorderColor = Color.Red
            ),
            visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = state.newPasswordError != null,
            supportingText = state.newPasswordError?.let { { Text(it, color = Color.Red) } },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LightGreen
                )
            },
            trailingIcon = {
                IconButton(onClick = { showNewPassword = !showNewPassword }) {
                    Icon(
                        imageVector = if (showNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (showNewPassword) "Hide password" else "Show password",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onEvent(ProfileUiEvent.ConfirmPasswordChanged(it)) },
            label = { Text("Confirm New Password *", color = Color.White.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = LightGreen,
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                cursorColor = LightGreen,
                errorBorderColor = Color.Red
            ),
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            isError = state.confirmPasswordError != null,
            supportingText = state.confirmPasswordError?.let { { Text(it, color = Color.Red) } },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LightGreen
                )
            },
            trailingIcon = {
                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                    Icon(
                        imageVector = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (showConfirmPassword) "Hide password" else "Show password",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Change/Cancel buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.onEvent(ProfileUiEvent.CancelPasswordChange) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
            ) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.onEvent(ProfileUiEvent.ChangePassword) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                shape = RoundedCornerShape(16.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = "Change",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LightGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

