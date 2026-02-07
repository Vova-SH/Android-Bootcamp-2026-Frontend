package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Golyboi
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.LGray
import ru.sicampus.bootcamp2026.ui.theme.LightGray
import ru.sicampus.bootcamp2026.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterLinkClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        LoginField(
            value = email,
            onValueChange = { email = it },
            label = "Почта",
            placeholder = "Введите почту..."
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            placeholder = "Введите пароль..."
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGray
            )
        ) {
            Text(
                text = "Вход",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(
                text = "Нет аккаунта? ",
                color = LightGray,
                fontSize = 16.sp
            )
            Text(
                text = "Зарегистрируйтесь",
                color = Golyboi,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable(onClick = onRegisterLinkClick)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 30.sp,
            color = White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Gray.copy(alpha = 0.5f),
                    fontSize = 18.sp
                )
            },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gray.copy(alpha = 0.5f),
                unfocusedBorderColor = LGray,
                focusedContainerColor = LightGray,
                unfocusedContainerColor = LightGray
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true, name = "Экран входа")
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen(
                onLoginClick = {},
                onRegisterLinkClick = {}
            )
        }
    }
}