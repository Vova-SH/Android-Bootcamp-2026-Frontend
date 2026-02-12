package ru.sicampus.bootcamp2026.screens.Login

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.ui.auth.LoginUiState
import ru.sicampus.bootcamp2026.ui.auth.LoginViewModel
import ru.sicampus.bootcamp2026.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory()
    )
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            navController.navigate("list") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

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
            value = login,
            onValueChange = { login = it },
            label = "Логин",
            placeholder = "Введите логин..."
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            placeholder = "Введите пароль...",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        when (uiState) {
            is LoginUiState.Error -> {
                Text(
                    text = (uiState as LoginUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            else -> {}
        }

        Button(
            onClick = {
                viewModel.login(login, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGray
            ),
            enabled = uiState != LoginUiState.Loading
        ) {
            if (uiState == LoginUiState.Loading) {
                CircularProgressIndicator(
                    color = White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Вход",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
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
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
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
    isPassword: Boolean = false
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
                unfocusedContainerColor = LightGray,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
    }
}