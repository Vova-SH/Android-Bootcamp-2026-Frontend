package ru.sicampus.bootcamp2026.android.ui.testScreens.auth

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.components.CustomTextField
import ru.sicampus.bootcamp2026.android.ui.theme.ErrorRed
import ru.sicampus.bootcamp2026.android.ui.theme.Grey

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController,
) {
    SecureScreen()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is AuthAction.OpenScreen -> navController.navigate(action.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(210.dp))

        Text(
            text = "neRakov MEET",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.open_sans_bold))
        )
        when (val currentState = state) {
            is AuthState.Data -> Content(viewModel, currentState, navController)
            is AuthState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Composable
private fun Content(
    viewModel: AuthViewModel,
    state: AuthState.Data,
    navController: NavController
) {
    var inputLogin by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    val focusPasswordRequester = remember { FocusRequester() }

    Spacer(modifier = Modifier.size(55.dp))

    CustomTextField(
        value = inputLogin,
        onValueChange = { newLogin ->
            inputLogin = newLogin
            viewModel.onIntent(
                AuthIntent.TextInput(
                    newLogin,
                    inputPassword
                )
            )
        },
        label = stringResource(R.string.email_text),
        modifier = Modifier,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        keyboardActionOnNext = {
            focusPasswordRequester.requestFocus()
        }
    )

    Spacer(modifier = Modifier.size(24.dp))

    CustomTextField(
        value = inputPassword,
        onValueChange = { newPassword ->
            inputPassword = newPassword
            viewModel.onIntent(
                AuthIntent.TextInput(
                    inputLogin,
                    newPassword
                )
            )
        },
        label = stringResource(R.string.password_text),
        modifier = Modifier,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        visualTransformation = PasswordVisualTransformation(),
        focusRequester = focusPasswordRequester,
        keyboardActionOnDone = {
            viewModel.onIntent(AuthIntent.Send(inputLogin, inputPassword))
        }
    )

    Spacer(modifier = Modifier.size(45.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 65.dp),
        onClick = {
            viewModel.onIntent(AuthIntent.Send(inputLogin, inputPassword))
        },
        enabled = state.isEnabledSend
    ) {
        Text(stringResource(R.string.sign_in_button))
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Кнопка "Зарегистрироваться"
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 65.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Grey
        ),
        onClick = {
            viewModel.onIntent(AuthIntent.NavigateToSignUp)
        }
    ) {
        Text("Зарегистрироваться")
    }

    if (state.error != null) {

        Spacer(modifier = Modifier.height(45.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .border(
                    width = 2.dp,
                    color = ErrorRed,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 13.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.error,
                color = ErrorRed,
                modifier = Modifier.align(Alignment.Center)
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