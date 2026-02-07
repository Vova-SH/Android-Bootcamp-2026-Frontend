package ru.sicampus.bootcamp2026.ui.screen.auth


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.coroutineScope
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Montserrat
import ru.sicampus.bootcamp2026.ui.theme.SineyIney
import ru.sicampus.bootcamp2026.ui.theme.buttonTextColor
import ru.sicampus.bootcamp2026.ui.theme.containerColor
import ru.sicampus.bootcamp2026.ui.theme.textColor

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when(action){
                is AuthAction.OpenScreen -> navController.navigate(action.route)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(start = 32.dp, end = 32.dp, top = 96.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(56.dp)
    ) {
        Text(
            text = "Авторизация",
            fontSize = 24.sp,
            color = Black,
            fontFamily = Montserrat,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        when (val currentState = state) {
            is AuthState.Data -> Content(viewModel, currentState, navController, onLoginSuccess)
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
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    var inputLogin by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    val focusPasswordRequester = remember { FocusRequester() }

    Spacer(modifier = Modifier.size(16.dp))
    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp),
            value = inputLogin,
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
                inputLogin = it
                viewModel.onIntent(AuthIntent.TextInput(inputLogin, inputPassword))
            },
            label = { Text("Почта", color = textColor, fontSize = 16.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor.copy(alpha = 0.5f),
            ),
            shape = RoundedCornerShape(30.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextField(
            modifier = Modifier
                .focusRequester(focusPasswordRequester)
                .fillMaxWidth()
                .height(51.dp),
            value = inputPassword,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.onIntent(AuthIntent.Send(inputLogin, inputPassword))
                }
            ),
            onValueChange = {
                inputPassword = it
                viewModel.onIntent(AuthIntent.TextInput(inputLogin, inputPassword))
            },
            label = { Text("Пароль", color = textColor, fontSize = 16.sp)},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor.copy(alpha = 0.5f),
            ),
            shape = RoundedCornerShape(30.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(51.dp),
            onClick = {
                viewModel.onIntent(AuthIntent.Send(inputLogin, inputPassword))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = SineyIney,
                disabledContainerColor = SineyIney//.copy(alpha = 0.38f),
            ),
        ) {
            Text(
                text = "Войти",
                fontSize = 16.sp,
                color = buttonTextColor,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }



        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
        ) {
            Text(
                text = "Нет аккаунта?",
                fontSize = 14.sp,
                color = Black,
                fontFamily = FontFamily.Default
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Зарегистрироваться",
                fontSize = 14.sp,
                color = SineyIney,
                fontFamily = FontFamily.Default,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
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



//@Preview(showBackground = true)
//@Composable
//fun AuthPreview() {
//    MaterialTheme(
//        typography = CustomTypography
//    ) {
//        Auth()
//    }
//}