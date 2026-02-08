package ru.sicampus.bootcamp2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.ui.components.JuicyBackground
import ru.sicampus.bootcamp2026.ui.components.JuicyButton
import ru.sicampus.bootcamp2026.ui.components.JuicyTextField
import ru.sicampus.bootcamp2026.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (UserRegisterDto) -> Unit,
    onBackClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var secondName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }

    JuicyBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    "Создать аккаутн",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextPrimary
                )
                Text(
                    "Вступи в команду уже сегодня",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(32.dp))

                JuicyTextField(value = firstName, onValueChange = { firstName = it }, label = "Имя", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                JuicyTextField(value = secondName, onValueChange = { secondName = it }, label = "Фамилия", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                JuicyTextField(value = email, onValueChange = { email = it }, label = "Email", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))

                val passwordColors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = BrandPrimary,
                    focusedLabelColor = BrandPrimary,
                    unfocusedLabelColor = TextSecondary
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль", style = MaterialTheme.typography.bodyMedium) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium),
                    shape = MaterialTheme.shapes.medium,
                    colors = passwordColors
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = passwordAgain,
                    onValueChange = { passwordAgain = it },
                    label = { Text("Повтори пароль", style = MaterialTheme.typography.bodyMedium) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium),
                    shape = MaterialTheme.shapes.medium,
                    colors = passwordColors
                )

                Spacer(modifier = Modifier.height(40.dp))

                JuicyButton(
                    onClick = {
                        onRegisterClick(
                            UserRegisterDto(
                                firstName = firstName.trim(),
                                secondName = secondName.trim(),
                                email = email.trim(),
                                password = password.trim(),
                                passwordAgain = passwordAgain.trim()
                            )
                        )
                    },
                    text = "Зарегистрироваться",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}