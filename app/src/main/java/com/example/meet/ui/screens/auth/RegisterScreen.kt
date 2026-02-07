package com.example.meet.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet.Screen
import com.example.meet.data.source.DataLocator
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(navController: NavHostController) {
    val ds = remember { DataLocator.userInfoDataSource }
    val scope = rememberCoroutineScope()


    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }


    var emailError by remember { mutableStateOf<String?>(null) }
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf<String?>(null) }


    fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            emailError = "Email обязателен"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Некорректный email"
            false
        } else {
            emailError = null
            true
        }
    }

    fun validateFullName(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            fullNameError = "ФИО обязательно"
            false
        } else if (fullName.trim().split("\\s+".toRegex()).size < 2) {
            fullNameError = "Введите имя и фамилию"
            false
        } else if (fullName.length < 3) {
            fullNameError = "ФИО слишком короткое"
            false
        } else {
            fullNameError = null
            true
        }
    }

    fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) {
            passwordError = "Пароль обязателен"
            false
        } else if (password.length < 8) {
            passwordError = "Пароль должен быть не менее 8 символов"
            false
        } else if (!password.any { it.isDigit() }) {
            passwordError = "Пароль должен содержать хотя бы одну цифру"
            false
        } else if (!password.any { it.isLetter() }) {
            passwordError = "Пароль должен содержать хотя бы одну букву"
            false
        } else {
            passwordError = null
            true
        }
    }


    fun validateAllFields(): Boolean {
        val isEmailValid = validateEmail(email.trim())
        val isFullNameValid = validateFullName(fullName.trim())
        val isPasswordValid = validatePassword(password)

        return isEmailValid && isFullNameValid && isPasswordValid
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Регистрация") }
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
                value = fullName,
                onValueChange = {
                    fullName = it
                    if (fullNameError != null) validateFullName(it.trim())
                },
                label = { Text("ФИО") },
                isError = fullNameError != null,
                supportingText = {
                    if (fullNameError != null) {
                        Text(text = fullNameError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError != null) validateEmail(it.trim())
                },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(text = emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) validatePassword(it)
                },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(text = passwordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = position,
                onValueChange = { position = it },
                label = { Text("Должность (необязательно)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                label = { Text("Отдел (необязательно)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!validateAllFields()) {
                        error = "Пожалуйста, исправьте ошибки в форме"
                        return@Button
                    }

                    scope.launch {
                        isLoading = true
                        error = null
                        success = null
                        try {
                            val created = ds.registerUser(
                                email = email.trim(),
                                password = password,
                                fullName = fullName.trim(),
                                position = position.trim().takeIf { it.isNotEmpty() },
                                department = department.trim().takeIf { it.isNotEmpty() }
                            )
                            success = "Пользователь создан: ${created.fullName}"
                            navController.navigate(Screen.MainMeet.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            error = "Ошибка: ${e.message ?: e.javaClass.simpleName}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(if (isLoading) "Создаём..." else "Зарегистрироваться")
            }

            if (success != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = success!!, color = MaterialTheme.colorScheme.primary)
            }
            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}