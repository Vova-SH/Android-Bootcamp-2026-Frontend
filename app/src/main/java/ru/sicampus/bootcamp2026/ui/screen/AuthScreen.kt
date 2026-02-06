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
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthUiState
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthViewModel

@Composable
fun AuthRoute(
    viewModel: AuthViewModel = viewModel(),
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            navigateToHome()
        }
    }

    if (uiState is AuthUiState.Error) {
        val errorMsg = (uiState as AuthUiState.Error).message
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            confirmButton = { TextButton(onClick = { viewModel.clearError() }) { Text("OK") } },
            title = { Text("Ошибка") },
            text = { Text(errorMsg) }
        )
    }

    AuthScreenContent(
        isLoading = uiState is AuthUiState.Loading,
        onLoginClick = { login, pass -> viewModel.login(login, pass) },
        onRegisterClick = navigateToRegister
    )
}

@Composable
fun AuthScreenContent(
    isLoading: Boolean,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("jdh@test.com") } // Можно убрать дефолтное значение
    var password by remember { mutableStateOf("admin") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Вход в систему", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotBlank() && password.isNotBlank()
                ) {
                    Text("Войти")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onRegisterClick) {
                    Text("Нет аккаунта? Зарегистрироваться")
                }
            }
        }
    }
}