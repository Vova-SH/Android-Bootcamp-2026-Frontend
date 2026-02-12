package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.AppButton
import ru.sicampus.bootcamp2026.components.InputField
import ru.sicampus.bootcamp2026.domain.entities.ProfileUpdateEntity
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun Profile(
    viewmodel: ProfileViewModel = viewModel(LocalActivity.current as ComponentActivity)
) {
    val state by viewmodel.uiState.collectAsState()
    val isEditMode by viewmodel.isEditMode.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { viewmodel.onIntent(ProfileIntent.Logout) }) {
                    Text("Logout", color = Color.Red)
                }
            }

            if (isEditMode) {
                ProfileEditMode(
                    state = state,
                    onCancel = { viewmodel.onIntent(ProfileIntent.Cancel) },
                    onSave = { viewmodel.onIntent(ProfileIntent.Save(it)) },
                    viewmodel = viewmodel
                )
            } else {
                ProfileViewMode(state = state, onRefresh = { viewmodel.onIntent(ProfileIntent.Load) })
            }
        }
    }
}


@Composable
fun ProfileViewMode(state: ProfileState,
                    onRefresh : () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        when (state) {
            is ProfileState.Loading -> CircularProgressIndicator()
            is ProfileState.Error -> {
                Text(state.reason, color = Color.Red)
                TextButton(onClick = {onRefresh() }) {
                    Text("Refresh")
                }
            }
            is ProfileState.Content -> {
                val user = state.user

                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    placeholder = ColorPainter(Color.LightGray),
                    error = painterResource(R.drawable.ic_profile)
                )
                Text(user.name, style = MaterialTheme.typography.headlineMedium)
                Text(user.position, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Username: ${user.username}")
                    Text("Email: ${user.email}")
                    Text("Phone: ${user.phoneNumber}")
                }
            }
        }
    }
}

@Composable
fun ProfileEditMode(
    state: ProfileState,
    onCancel: () -> Unit,
    viewmodel: ProfileViewModel,
    onSave: (ProfileUpdateEntity) -> Unit
) {
    when (state) {
        is ProfileState.Content -> {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                InputField(
                    title = "ФИЛ",
                    state = viewmodel.nameState,
                    placeholderText = "Введите ФИО",
                    imeAction = ImeAction.Next
                )

                InputField(
                    title = "Должность",
                    state = viewmodel.positionState,
                    placeholderText = "Введите Должность",
                    imeAction = ImeAction.Next
                )

                InputField(
                    title = "Почта",
                    state = viewmodel.emailState,
                    placeholderText = "Введите Почту",
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )

                InputField(
                    title = "Телефон",
                    state = viewmodel.phoneState,
                    placeholderText = "Введите телефон",
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppButton(
                        text = "Сохранить",
                        onClick = {
                            val updateData = ProfileUpdateEntity(
                                name = viewmodel.nameState.text.toString(),
                                position = viewmodel.positionState.text.toString(),
                                email = viewmodel.emailState.text.toString(),
                                phoneNumber = viewmodel.phoneState.text.toString()
                            )
                            onSave(updateData)
                        }
                    )
                    AppButton(
                        text = "Отмена",
                        onClick = {
                            onCancel()
                        },
                        activeColors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = Black
                        )
                    )
                }
            }
        }
        is ProfileState.Error -> {
            Text(state.reason, color = Color.Red)
            TextButton(onClick = { onCancel() }) {
                Text("Назад")
            }
        }
        is ProfileState.Loading -> CircularProgressIndicator()
    }
}