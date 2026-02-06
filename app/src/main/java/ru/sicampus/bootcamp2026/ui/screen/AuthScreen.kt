package ru.sicampus.bootcamp2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthUiState
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthViewModel

@Composable
fun AuthRoute(
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            navigateToHome()
        }
    }

    AuthScreen(
        uiState = uiState,
        onLoginClick = { email, pass -> viewModel.login(email, pass) },
        onRegisterClick = navigateToRegister,
        onClearError = { viewModel.clearError() }
    )
}

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onClearError: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (uiState is AuthUiState.Error) {
        AlertDialog(
            onDismissRequest = onClearError,
            confirmButton = { TextButton(onClick = onClearError) { Text("OK") } },
            title = { Text("Ошибка") },
            text = { Text(uiState.message) }
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Вход", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onLoginClick(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is AuthUiState.Loading
            ) {
                Text("Войти")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onRegisterClick) {
                Text("Нет аккаунта? Зарегистрироваться")
            }
        }

        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator()
        }
    }
}