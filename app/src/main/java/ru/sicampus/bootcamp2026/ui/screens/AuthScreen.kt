package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.data.model.AuthScreenType
import ru.sicampus.bootcamp2026.data.model.AuthUiState
import ru.sicampus.bootcamp2026.data.model.AuthViewModel
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    state: AuthUiState,
    onRegister: (String, String, String) -> Unit,
    onLogin: (String, String) -> Unit
) {
    val vmState = viewModel.state.collectAsState().value

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val position by remember { mutableStateOf("Сотрудник") }
    var tokenCode by remember { mutableStateOf("") }

    val mainButtonColor = Color(0xFF8B5CF6)
    val secondaryButtonColor = Color(0xFFF3F4F6)
    val secondaryTextColor = Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val titleText = when (vmState.screenType) {
            AuthScreenType.LOGIN -> "Войдите в аккаунт"
            AuthScreenType.REGISTER -> "Создайте аккаунт"
            AuthScreenType.EMAIL_CONFIRM -> "Введите код"
            AuthScreenType.FORGOT_PASSWORD -> "Сброс пароля"
            AuthScreenType.RESET_PASSWORD -> "Новый пароль"
        }

        Text(
            text = titleText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (vmState.screenType == AuthScreenType.EMAIL_CONFIRM) {
            Text(
                text = "Код отправлен на\n${vmState.emailForConfirmation}",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AuthInput(value = tokenCode, onValueChange = { tokenCode = it }, placeholder = "Код из письма")
        }

        if (vmState.screenType == AuthScreenType.REGISTER) {
            AuthInput(value = fullName, onValueChange = { fullName = it }, placeholder = "ФИО")
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (vmState.screenType == AuthScreenType.LOGIN || vmState.screenType == AuthScreenType.REGISTER || vmState.screenType == AuthScreenType.FORGOT_PASSWORD) {
            AuthInput(
                value = email,
                onValueChange = { email = it },
                placeholder = if (vmState.screenType == AuthScreenType.LOGIN) "Почта" else "Почта",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (vmState.screenType == AuthScreenType.RESET_PASSWORD) {
            AuthInput(value = tokenCode, onValueChange = { tokenCode = it }, placeholder = "Токен из письма")
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (vmState.screenType == AuthScreenType.LOGIN || vmState.screenType == AuthScreenType.REGISTER || vmState.screenType == AuthScreenType.RESET_PASSWORD) {
            AuthInput(
                value = password,
                onValueChange = { password = it },
                placeholder = if (vmState.screenType == AuthScreenType.RESET_PASSWORD) "Новый пароль" else "Пароль",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (vmState.error != null) {
            Text(vmState.error, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (vmState.successMessage != null) {
            Text(vmState.successMessage, color = Color(0xFF008800), fontSize = 14.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        val mainBtnText = when (vmState.screenType) {
            AuthScreenType.LOGIN -> "Вход"
            AuthScreenType.REGISTER -> "Регистрация"
            AuthScreenType.EMAIL_CONFIRM -> "Подтвердить"
            AuthScreenType.FORGOT_PASSWORD -> "Сбросить пароль"
            AuthScreenType.RESET_PASSWORD -> "Сохранить"
        }

        Button(
            onClick = {
                when (vmState.screenType) {
                    AuthScreenType.LOGIN -> viewModel.login(email.trim(), password)
                    AuthScreenType.REGISTER -> viewModel.register(fullName.trim(), email.trim(), password, position)
                    AuthScreenType.EMAIL_CONFIRM -> viewModel.confirmEmail(tokenCode.trim())
                    AuthScreenType.FORGOT_PASSWORD -> viewModel.forgotPassword(email.trim())
                    AuthScreenType.RESET_PASSWORD -> viewModel.resetPassword(tokenCode.trim(), password)
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = mainButtonColor),
            enabled = !vmState.isLoading
        ) {
            if (vmState.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text(mainBtnText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (vmState.screenType == AuthScreenType.LOGIN) {
            SecondaryButton(text = "Регистрация", color = secondaryButtonColor, textColor = secondaryTextColor) {
                viewModel.switchScreen(AuthScreenType.REGISTER)
            }
            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(text = "Я не помню пароль", color = secondaryButtonColor, textColor = secondaryTextColor) {
                viewModel.switchScreen(AuthScreenType.FORGOT_PASSWORD)
            }
        } else if (vmState.screenType == AuthScreenType.REGISTER) {
            SecondaryButton(text = "Вход", color = secondaryButtonColor, textColor = secondaryTextColor) {
                viewModel.switchScreen(AuthScreenType.LOGIN)
            }
        } else {
            SecondaryButton(text = "Вернуться ко входу", color = secondaryButtonColor, textColor = secondaryTextColor) {
                viewModel.switchScreen(AuthScreenType.LOGIN)
            }
        }
    }
}

@Composable
fun SecondaryButton(text: String, color: Color, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = textColor),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AuthInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray.copy(alpha = 0.7f)) },
        singleLine = true,
        isError = false,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .customDashedBorder(color = Color.LightGray, cornerRadius = 12.dp)
    )
}