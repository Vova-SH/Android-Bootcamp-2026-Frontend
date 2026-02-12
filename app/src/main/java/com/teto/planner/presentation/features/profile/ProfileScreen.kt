package com.teto.planner.presentation.features.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.presentation.common.CropKitAvatarCropperDialog
import com.teto.planner.presentation.common.openTelegramChat
import com.teto.planner.presentation.theme.AppTheme

@Composable
fun ProfileScreen(
    onSave: () -> Unit,
    onEdit: () -> Unit,
    onBack: () -> Unit,
    onExit: () -> Unit,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var croppingUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            croppingUri = uri
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is ProfileScreenUiState.Success && (uiState as ProfileScreenUiState.Success).isSaveSuccessful) {
            onSave()
        }
    }

    if (croppingUri != null) {
        CropKitAvatarCropperDialog(
            imageUri = croppingUri!!,
            onDismiss = { croppingUri = null },
            onCropSuccess = { bytes ->
                viewModel.updateAvatar(bytes)
                croppingUri = null
            }
        )
    }

    when (val state = uiState) {
        is ProfileScreenUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ProfileScreenUiState.Success -> {
            ProfileContent(
                state = state,
                onNameChange = viewModel::onNameChange,
                onTelegramChange = viewModel::onTelegramChange,
                onBioChange = viewModel::onBioChange,
                onEditClick = {
                    viewModel.startEditing()
                    onEdit()
                },
                onSaveClick = viewModel::saveProfile,
                onBack = onBack,
                onExit = {
                    viewModel.logout()
                    onExit()
                },
                onAvatarClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
        }

        is ProfileScreenUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileScreenUiState.Success,
    onNameChange: (String) -> Unit,
    onTelegramChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onBack: () -> Unit,
    onExit: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мой профиль", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (state.telegram.isNotBlank()) {
                        IconButton(
                            onClick = {
                                context.openTelegramChat(state.telegram)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Open Telegram",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = onExit) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Выйти из аккаунта",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(top = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable(onClick = onAvatarClick),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(0.6f),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    val fullAvatarUrl = remember(state.user.avatarUrl, state.user.updatedAt) {
                        state.user.avatarUrl?.let { url ->
                            val version = state.user.updatedAt?.hashCode() ?: ""
                            "$url?v=$version"
                        }
                    }

                    if (!fullAvatarUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(fullAvatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    if (state.isAvatarUploading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                if (!state.isAvatarUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(28.dp)
                                .background(MaterialTheme.colorScheme.surface, CircleShape)
                                .padding(2.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit avatar",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            TextField(
                value = state.user.login,
                onValueChange = {},
                label = { Text("Логин") },
                readOnly = true,
                enabled = !state.isEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .alpha(if (state.isEditing) 0.5f else 1f)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            TextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Имя") },
                readOnly = !state.isEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            TextField(
                value = state.telegram,
                onValueChange = onTelegramChange,
                label = { Text("Telegram") },
                readOnly = !state.isEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            TextField(
                value = state.bio,
                onValueChange = onBioChange,
                label = { Text("Описание") },
                readOnly = !state.isEditing,
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isEditing
                ) {
                    Text("Редактировать")
                }

                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.canSave
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    AppTheme {
        ProfileContent(
            state = ProfileScreenUiState.Success(
                user = UserMe(
                    id = "1",
                    login = "oatis1996",
                    name = "Oatis",
                    bio = null,
                    telegram = null,
                    avatarUrl = null,
                    roles = emptyList(),
                    updatedAt = null
                ),
                name = "Oatis",
                isEditing = false
            ),
            onNameChange = {}, onTelegramChange = {}, onBioChange = {},
            onEditClick = {}, onSaveClick = {},
            onBack = {},
            onExit = {},
            onAvatarClick = {}
        )
    }
}