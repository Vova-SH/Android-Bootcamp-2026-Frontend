package ru.sicampus.bootcamp2026.presentation.ui.screens.main.profile

import android.content.res.Configuration
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
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

enum class ProfileMode {
    DISPLAY,
    EDIT
}

@Composable
fun ProfileInfoScreen() {
    var mode by rememberSaveable { mutableStateOf(ProfileMode.DISPLAY) }

    var savedProfile by remember {
        mutableStateOf(
            ProfileData(
                login = "Input",
                phone = "+7 900 123-45-67",
                email = "example@domain.com",
                position = "Input",
                about = "Например, Senior UX Designer..."
            )
        )
    }

    ProfileContent(
        mode = mode,
        savedProfile = savedProfile,
        onBackClick = { mode = ProfileMode.DISPLAY },
        onEditClick = { mode = ProfileMode.EDIT },
        onSaveClick = { newProfileData ->
            savedProfile = newProfileData
            mode = ProfileMode.DISPLAY
        }
    )
}

data class ProfileData(
    val login: String = "",
    val phone: String = "",
    val email: String = "",
    val position: String = "",
    val about: String = ""
)

@Composable
internal fun ProfileContent(
    mode: ProfileMode,
    savedProfile: ProfileData,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: (ProfileData) -> Unit
) {
    var login by rememberSaveable { mutableStateOf(savedProfile.login) }
    var phone by rememberSaveable { mutableStateOf(savedProfile.phone) }
    var email by rememberSaveable { mutableStateOf(savedProfile.email) }
    var position by rememberSaveable { mutableStateOf(savedProfile.position) }
    var about by rememberSaveable { mutableStateOf(savedProfile.about) }

    LaunchedEffect(mode, savedProfile) {
        if (mode == ProfileMode.EDIT) {
            login = savedProfile.login
            phone = savedProfile.phone
            email = savedProfile.email
            position = savedProfile.position
            about = savedProfile.about
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mode == ProfileMode.EDIT) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Text(
                text = "Профиль",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (mode == ProfileMode.DISPLAY) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        onSaveClick(
                            ProfileData(
                                login = login,
                                phone = phone,
                                email = email,
                                position = position,
                                about = about
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Сохранить",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (mode == ProfileMode.DISPLAY) {
                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
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

            if (mode == ProfileMode.DISPLAY) {
                val displayFields = listOf(
                    "Логин" to savedProfile.login,
                    "Телефон" to savedProfile.phone,
                    "Email" to savedProfile.email,
                    "Должность" to savedProfile.position,
                    "О себе" to savedProfile.about
                )

                displayFields.forEachIndexed { index, (label, value) ->
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

@Preview(
    name = "Display Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileInfoScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProfileInfoScreen()
        }
    }
}

@Preview(
    name = "Edit Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileInfoScreenEditPreview() {
    AndroidBootcamp2026FrontendTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var mode by remember { mutableStateOf(ProfileMode.EDIT) }
            var savedProfile by remember {
                mutableStateOf(
                    ProfileData(
                        login = "Input",
                        phone = "+7 900 123-45-67",
                        email = "example@domain.com",
                        position = "Input",
                        about = "Например, Senior UX Designer..."
                    )
                )
            }

            ProfileContent(
                mode = mode,
                savedProfile = savedProfile,
                onBackClick = { mode = ProfileMode.DISPLAY },
                onEditClick = { mode = ProfileMode.EDIT },
                onSaveClick = { newData ->
                    savedProfile = newData
                    mode = ProfileMode.DISPLAY
                }
            )
        }
    }
}

@Preview(
    name = "Dark Mode - Display",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileInfoScreenDarkPreview() {
    AndroidBootcamp2026FrontendTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProfileInfoScreen()
        }
    }
}