package ru.sicampus.bootcamp2026.ui.screens.auth

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkGrey
import ru.sicampus.bootcamp2026.ui.theme.LightGrey
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun AuthorizationScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    SecureScreen()
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when(action) {
                is AuthAction.OpenScreen -> navController.navigate(action.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
            .verticalScroll(scrollState))
    {
        Spacer(modifier = Modifier.height(100.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .padding(24.dp)) {
            Text(text = "С возвращением!",
                fontSize = 33.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = White
            )
        }

        Spacer(modifier = Modifier.height(100.dp))

        Card(shape = RoundedCornerShape(topStart = 30.dp,
            topEnd = 30.dp),
            colors = CardDefaults.cardColors(White),
            modifier = Modifier.fillMaxSize()

        ) {
            Spacer(modifier = Modifier.height(35.dp))

            when (val currentState = state) {
                is AuthState.Data -> Content(viewModel, currentState)
                is AuthState.Loading -> {
                    CircularProgressIndicator()
                }
            }

            Spacer(modifier = Modifier.height(150.dp))

        }

    }
}

@Composable
fun Content(
    viewModel: AuthViewModel,
    state: AuthState.Data
) {
    var login by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    val focusPasswordRequester = remember { FocusRequester() }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = login,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusPasswordRequester.requestFocus()
            }
        ),
        onValueChange = {
            login = it
            viewModel.onIntent(AuthIntent.TextInput(login, password))
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Black ,
            unfocusedTextColor = Black,
            disabledTextColor = Black,
            focusedContainerColor = LightGrey,
            unfocusedContainerColor = LightGrey,
            cursorColor = Black
        ),
        shape = RoundedCornerShape(20.dp),
        label = { Text("Введите email",fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Blue) },
        leadingIcon = { Icon(
            painter = painterResource(id = R.drawable.email), contentDescription = "Иконка",
            tint = DarkGrey,
            modifier = Modifier.padding(end=5.dp))}
    )
    Spacer(modifier = Modifier.height(20.dp))
    TextField(
        modifier = Modifier.focusRequester(focusPasswordRequester).fillMaxWidth(),
        value = password,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                viewModel.onIntent(AuthIntent.Send(login, password))
            }
        ),
        onValueChange = {
            password = it
            viewModel.onIntent(AuthIntent.TextInput(login, password))
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Black ,
            unfocusedTextColor = Black,
            disabledTextColor = Black,
            focusedContainerColor = LightGrey,
            unfocusedContainerColor = LightGrey,
            cursorColor = Black
        ),
        shape = RoundedCornerShape(20.dp),
        label = { Text("Введите пароль", fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Blue) },
        leadingIcon = { Icon(
            painter = painterResource(id = R.drawable.password), contentDescription = "Иконка",
            tint = DarkGrey,
            modifier = Modifier.padding(end=5.dp))}
    )
    Spacer(modifier = Modifier.height(50.dp))
    Button(
        modifier = Modifier
            .height(63.dp)
            .width(270.dp),
        onClick = {
            viewModel.onIntent(AuthIntent.Send(login, password))
        },
        enabled = state.isEnabledSend,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(Blue)
    ) {
        Text(text="Войти", color= White,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)))
        Icon(
            painter = painterResource(R.drawable.arrow),
            contentDescription = "Вход",
            tint = White,
            modifier = Modifier.padding(start=6.dp))
    }
    if (state.error != null) {
        Text(
            modifier = Modifier,
            text = state.error,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red,
        )
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