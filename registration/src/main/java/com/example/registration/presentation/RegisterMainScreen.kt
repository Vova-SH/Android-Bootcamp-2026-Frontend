package com.example.registration.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterMainScreen(
    registerViewModel: RegisterScreenViewModel,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by registerViewModel.state.collectAsState()

    val isLoading = uiState is RegisterUiState.Loading
    val errorMessage = (uiState as? RegisterUiState.Error)?.message

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RegisterForm(
            fullName = registerViewModel.fullName,
            phoneNumber = registerViewModel.phoneNumber,
            department = registerViewModel.department,
            password = registerViewModel.password,
            onFullNameChange = { registerViewModel.onFullNameChange(it) },
            onPhoneNumberChange = { registerViewModel.onPhoneNumberChange(it) },
            onDepartmentChange = { registerViewModel.onDepartmentChange(it) },
            onPasswordChange = { registerViewModel.onPasswordChange(it) },
            onRegisterClicked = { registerViewModel.register() },
            onBack = onBack,
            errorMessage = errorMessage,
            enabled = !isLoading
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        if (uiState is RegisterUiState.Success) {
            onRegisterSuccess()
        }
    }
}



@Composable
fun RegisterForm(
    fullName: String,
    phoneNumber: String,
    department: String,
    password: String,
    onFullNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onDepartmentChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    onBack: () -> Unit,
    errorMessage: String? = null,
    enabled: Boolean = true
) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("Телефон") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = department,
            onValueChange = onDepartmentChange,
            label = { Text("Департамент") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onRegisterClicked,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text("Зарегистрироваться")
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text("Назад")
        }
    }
}
