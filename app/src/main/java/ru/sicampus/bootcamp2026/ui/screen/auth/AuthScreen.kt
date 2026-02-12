package ru.sicampus.bootcamp2026.ui.screen.auth

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.NavRoutes
import ru.sicampus.bootcamp2026.ui.theme.Red

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is AuthAction.OpenScreen ->
                    navController.navigate(NavRoutes.Meetings.route)
            }
        }
    }
    if (state is AuthState.Data) {
        Content(viewModel, state as AuthState.Data)
    }
}

@Composable
private fun Content(
    viewModel: AuthViewModel,
    state: AuthState.Data
) {
    var inputEmail by remember() { mutableStateOf("") }
    var inputPassword by remember() { mutableStateOf("") }
    val focusPasswordRequester = remember { FocusRequester() }
    Column(modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = inputEmail,
            keyboardActions = KeyboardActions(
                onNext = { focusPasswordRequester.requestFocus() }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                inputEmail = it
                viewModel.onIntent(AuthIntent.TextInput(inputEmail, inputPassword))
            },
            label = { Text("Почта") },
            placeholder = { Text("mail@example.com") })
        TextField(
            value = inputPassword,
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.onIntent(AuthIntent.Send(inputEmail, inputPassword))
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            onValueChange = { newValue -> // ← используем newValue
                inputPassword = newValue // ← правильно сохраняем пароль
                viewModel.onIntent(AuthIntent.TextInput(inputEmail, newValue))
            },
            label = { Text("Пароль") },
            placeholder = { Text("********") }
        )
        Button(onClick = {
            viewModel.onIntent(AuthIntent.Send(inputEmail, inputPassword))
        },
            enabled = state.isEnabledSend) {
            Text("Войти")
        }
        if (state.error != null) {
            Text(text = state.error, modifier = Modifier,
                color = Red
            )
        }
    }
}

@Composable
fun SecureScreen() {
    val activity = LocalActivity.current
    LifecycleStartEffect(Unit) {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        onStopOrDispose {
            activity?.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }
}

@Preview(device = "id:pixel_9_pro_xl")
@Composable
fun ShowAuth() {
//    AuthScreen()
}