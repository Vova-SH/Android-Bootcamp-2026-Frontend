package ru.sicampus.bootcamp2026.ui.screen.auth

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.navigation.Routes
import ru.sicampus.bootcamp2026.components.SimpleButton
import ru.sicampus.bootcamp2026.components.SimpleTextField
import ru.sicampus.bootcamp2026.components.RefText
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel<AuthViewModel>()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isAuthorized) {
        if (state.isAuthorized) {
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Auth.route) { inclusive = true }
            }
        }
    }
    
    AuthScreenContent(
        state = state,
        navController = navController,
        onEmailChange = { email -> 
            viewModel.onIntent(AuthIntent.TextInput(email, state.password)) 
        },
        onPasswordChange = { password -> 
            viewModel.onIntent(AuthIntent.TextInput(state.email, password)) 
        },
        onLoginClick = { 
            viewModel.onIntent(AuthIntent.Send(state.email, state.password)) 
        }
    )
}

@Composable
private fun AuthScreenContent(
    state: AuthState,
    navController: NavHostController,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
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
                text = "Авторизация",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(50.dp))

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

            Spacer(modifier = Modifier.height(25.dp))

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

            Spacer(modifier = Modifier.height(50.dp))

            SimpleButton("Войти", enabled = state.isEnabledSend) {
                onLoginClick()
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row {
                Text(
                    text = "Нет аккаунта? ",
                    fontSize = 16.sp
                )
                RefText(
                    text = "Зарегистрируйтесь",
                    onClick = {
                        navController.navigate(Routes.Registration.route)
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
fun PreviewAuthScreen() {
    AndroidBootcamp2026FrontendTheme {
        AuthScreenContent(
            state = AuthState(),
            navController = rememberNavController(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}
