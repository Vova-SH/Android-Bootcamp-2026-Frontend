package ru.sicampus.bootcamp2026.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.components.*
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthUiState
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthViewModel
import ru.sicampus.bootcamp2026.ui.theme.*

@Composable
fun AuthRoute(
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) { if (uiState is AuthUiState.Success) navigateToHome() }

    JuicyBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MainGradient, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "С возвращением",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            Text(
                "Войдите, чтобы продолжить",
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            JuicyTextField(value = email, onValueChange = { email = it }, label = "Email", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = OutlineLight,
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceLight
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            JuicyButton(
                text = "Войти",
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = navigateToRegister) {
                Text("Нет аккаунта? Создать", color = BrandPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}