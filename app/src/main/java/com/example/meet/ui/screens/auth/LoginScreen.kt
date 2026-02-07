package com.example.meet.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.example.meet.Screen
import com.example.meet.data.source.Network
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.regex.Pattern

@ExperimentalSerializationApi
@ExperimentalMaterial3Api
@Composable
fun LoginScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var infoMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email обязателен для заполнения"
            !isValidEmail(email.trim()) -> "Введите корректный email адрес"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Пароль обязателен для заполнения"
            password.length < 8 -> "Пароль должен содержать минимум 8 символов"
            else -> null
        }
    }

    fun validateAllFields(): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        emailError = emailValidation
        passwordError = passwordValidation

        return emailValidation == null && passwordValidation == null
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Вход") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError != null) {
                        emailError = validateEmail(it)
                    }
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) {
                        passwordError = validatePassword(it)
                    }
                },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(passwordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    emailError = null
                    passwordError = null
                    error = null
                    infoMessage = null

                    if (!validateAllFields()) {
                        return@Button
                    }

                    scope.launch {
                        isLoading = true

                        try {
                            val user = Network.login(email.trim(), password)
                            infoMessage = "Вы вошли как: ${user.fullName}"

                            navController.navigate(Screen.MainMeet.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            val msg = e.message ?: ""
                            error = when {
                                "401" in msg || "Unauthorized" in msg -> "Неверный email или пароль"
                                "Bad credentials" in msg -> "Неверный email или пароль"
                                else -> "Ошибка: ${e.message ?: e.javaClass.simpleName}"
                            }
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Входим..." else "Войти")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { navController.navigate(Screen.Register.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Нет аккаунта? Зарегистрироваться")
            }

            if (infoMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = infoMessage!!, color = MaterialTheme.colorScheme.primary)
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


fun isValidEmail(email: String): Boolean {
    val emailRegex = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    return emailRegex.matcher(email).matches()
}