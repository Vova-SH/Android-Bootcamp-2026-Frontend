package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.data.model.AuthUiState
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder

@Composable
fun AuthScreen(
    state: AuthUiState,
    onRegister: (fullName: String, email: String, password: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit
) {
    var isRegister by remember { mutableStateOf(true) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val primaryPurple = Color(0xFF7E57FF)
    val lightButton = Color(0xFFF3F4F6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRegister) "Register" else "Login",
            color = Color.Black.copy(alpha = 0.35f),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = if (isRegister) "Создайте аккаунт" else "Войдите в аккаунт",
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            lineHeight = 38.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 34.dp),
        )

        if (isRegister) {
            DashedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "ФИО",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        DashedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = if (isRegister) "Почта" else "Почта / Номер телефона",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Spacer(modifier = Modifier.height(14.dp))

        DashedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Пароль",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isPassword = true
        )

        if (state.error != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                if (isRegister) onRegister(fullName, email, password)
                else onLogin(email, password)
            },
            enabled = !state.loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryPurple,
                contentColor = Color.White,
                disabledContainerColor = primaryPurple.copy(alpha = 0.6f),
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = if (state.loading) "Подождите..." else if (isRegister) "Регистрация" else "Вход",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = { isRegister = !isRegister },
            colors = ButtonDefaults.buttonColors(
                containerColor = lightButton,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = if (isRegister) "Вход" else "Регистрация",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (!isRegister) {
            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { /* TODO: forgot password */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightButton,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Я не помню пароль",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun DashedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    isPassword: Boolean = false
) {
    val shape = RoundedCornerShape(14.dp)

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Black.copy(alpha = 0.55f)
            )
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .customDashedBorder()
    )
}
