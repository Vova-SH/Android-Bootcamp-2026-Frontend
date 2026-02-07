package ru.sicampus.bootcamp2026.screens.Register

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.screens.Login.LoginField
import ru.sicampus.bootcamp2026.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory()
    )
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            navController.navigate("list") {
                popUpTo("register") { inclusive = true }
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
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Регистрация",
            fontSize = 32.sp,
            color = White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LoginField(
            value = login,
            onValueChange = { login = it },
            label = "Логин",
            placeholder = "Введите логин..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoginField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            placeholder = "Введите пароль...",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoginField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Подтвердите пароль",
            placeholder = "Повторите пароль...",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoginField(
            value = name,
            onValueChange = { name = it },
            label = "Имя",
            placeholder = "Введите ваше имя..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoginField(
            value = lastName,
            onValueChange = { lastName = it },
            label = "Фамилия",
            placeholder = "Введите вашу фамилию..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoginField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "Введите email..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoginField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "Телефон",
            placeholder = "Введите номер телефона..."
        )

        Spacer(modifier = Modifier.height(40.dp))

        when (uiState) {
            is RegisterUiState.Error -> {
                Text(
                    text = (uiState as RegisterUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            else -> {}
        }

        Button(
            onClick = {
                viewModel.register(
                    login = login,
                    password = password,
                    confirmPassword = confirmPassword,
                    name = name,
                    lastName = lastName,
                    email = email,
                    phoneNumber = phoneNumber
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGray
            ),
            enabled = uiState != RegisterUiState.Loading
        ) {
            if (uiState == RegisterUiState.Loading) {
                CircularProgressIndicator(
                    color = White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Зарегистрироваться",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(
                text = "Уже есть аккаунт? ",
                color = LightGray,
                fontSize = 16.sp
            )
            Text(
                text = "Войти",
                color = Golyboi,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }
    }
}