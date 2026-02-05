package ru.sicampus.bootcamp2026.presentation.ui.screens.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

enum class ProfileMode {
    DISPLAY,
    EDIT
}

@Composable
fun ProfileScreen(
    nav: NavHostController,
    mode: ProfileMode = ProfileMode.DISPLAY
) {
    ProfileContent(
        mode = mode,
        onBackClick = { nav.popBackStack() },
        onEditClick = { /* TODO: Переключить в режим EDIT */ },
        onSaveClick = { /* TODO: Сохранить и перейти в DISPLAY */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    mode: ProfileMode,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var login by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var position by rememberSaveable { mutableStateOf("") }
    var about by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (mode == ProfileMode.DISPLAY) "User name" else "Профиль",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    if (mode == ProfileMode.DISPLAY) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Редактировать"
                            )
                        }
                    } else {
                        IconButton(onClick = onSaveClick) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Сохранить"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, null) },
                    label = { Text("Уведомления") },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Event, null) },
                    label = { Text("Встречи") },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Профиль") },
                    selected = true,
                    onClick = { }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (mode == ProfileMode.DISPLAY) {
                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Аватар
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Камера
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Изменить фото",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            val fields = listOf(
                "Логин" to (if (mode == ProfileMode.EDIT) login else "Input"),
                "Телефон" to (if (mode == ProfileMode.EDIT) phone else "+7 900 123-45-67"),
                "Email" to (if (mode == ProfileMode.EDIT) email else "example@domain.com"),
                "Должность" to (if (mode == ProfileMode.EDIT) position else "Input"),
                "О себе" to (if (mode == ProfileMode.EDIT) about else "Например, Senior UX Designer...")
            )

            if (mode == ProfileMode.DISPLAY) {
                val fields = listOf(
                    "Логин" to "Input",
                    "Телефон" to "+7 900 123-45-67",
                    "Email" to "example@domain.com",
                    "Должность" to "Input",
                    "О себе" to "Например, Senior UX Designer..."
                )

                fields.forEachIndexed { index, (label, value) ->
                    if (index > 0) Spacer(modifier = Modifier.height(16.dp))
                    ProfileDisplayField(label = label, value = value)
                }
            } else {
                ProfileEditField(
                    label = "Логин",
                    value = login,
                    onValueChange = { login = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
                ProfileEditField(
                    label = "Телефон",
                    value = phone,
                    onValueChange = { phone = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
                ProfileEditField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
                ProfileEditField(
                    label = "Должность",
                    value = position,
                    onValueChange = { position = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
                ProfileEditField(
                    label = "О себе",
                    value = about,
                    onValueChange = { about = it },
                    isMultiLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileDisplayField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ProfileEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isMultiLine: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = !isMultiLine,
        minLines = if (isMultiLine) 3 else 1,
        maxLines = if (isMultiLine) 5 else 1,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenDisplayPreview() {
    AndroidBootcamp2026FrontendTheme {
        ProfileContent(
            mode = ProfileMode.DISPLAY,
            onBackClick = {},
            onEditClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenEditPreview() {
    AndroidBootcamp2026FrontendTheme {
        ProfileContent(
            mode = ProfileMode.EDIT,
            onBackClick = {},
            onEditClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenDarkPreview() {
    AndroidBootcamp2026FrontendTheme {
        ProfileContent(
            mode = ProfileMode.DISPLAY,
            onBackClick = {},
            onEditClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF111318,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenEditDarkPreview() {
    AndroidBootcamp2026FrontendTheme {
        ProfileContent(
            mode = ProfileMode.EDIT,
            onBackClick = {},
            onEditClick = {},
            onSaveClick = {}
        )
    }
}