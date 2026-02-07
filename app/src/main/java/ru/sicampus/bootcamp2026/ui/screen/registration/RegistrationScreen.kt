package ru.sicampus.bootcamp2026.ui.screen.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.components.RefText
import ru.sicampus.bootcamp2026.components.SimpleButton
import ru.sicampus.bootcamp2026.components.SimpleTextField
import ru.sicampus.bootcamp2026.navigation.Routes
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    viewModel: RegistrationViewModel = viewModel<RegistrationViewModel>()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Auth.route) { inclusive = true }
            }
        }
    }

    RegistrationScreenContent(
        state = state,
        navController = navController,
        onNameChange = { name ->
            viewModel.onIntent(RegistrationIntent.TextInput(name, state.login, state.email, state.password))
        },
        onLoginChange = { login ->
            viewModel.onIntent(RegistrationIntent.TextInput(state.name, login, state.email, state.password))
        },
        onEmailChange = { email ->
            viewModel.onIntent(RegistrationIntent.TextInput(state.name, state.login, email, state.password))
        },
        onPasswordChange = { password ->
            viewModel.onIntent(RegistrationIntent.TextInput(state.name, state.login, state.email, password))
        },
        onRegisterClick = {
            viewModel.onIntent(RegistrationIntent.Register(state.name, state.login, state.email, state.password))
        }
    )
}

@Composable
private fun RegistrationScreenContent(
    state: RegistrationState,
    navController: NavHostController,
    onNameChange: (String) -> Unit,
    onLoginChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Регистрация",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(30.dp))

            SimpleTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = "Имя",
                leadingIcon = {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))


            SimpleTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = "Электронная почта",
                leadingIcon = {
                    Image(
                        imageVector = Icons.Default.Email,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SimpleTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = "Пароль",
                leadingIcon = {
                    Image(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                },
                visualTransformation = PasswordVisualTransformation()
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            SimpleButton("Зарегистрироваться", enabled = state.isEnabledRegister) {
                onRegisterClick()
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row {
                Text(
                    text = "Уже есть аккаунт? ",
                    fontSize = 16.sp
                )
                RefText(
                    text = "Войдите",
                    onClick = {
                        navController.navigate(Routes.Auth.route)
                    }
                )
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewRegistrationScreen() {
    AndroidBootcamp2026FrontendTheme {
        RegistrationScreenContent(
            state = RegistrationState(),
            navController = rememberNavController(),
            onNameChange = {},
            onLoginChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRegisterClick = {}
        )
    }
}
