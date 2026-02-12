package ru.sicampus.bootcamp2026.ui.screen.auth


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.platform.LocalContext
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
import ru.sicampus.bootcamp2026.ui.nav.RegisterRoute
import ru.sicampus.bootcamp2026.ui.screen.register.InputField
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography
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
    val context = LocalContext.current

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
            text = "Здравствуйте!",
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
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        if (state.error != null) {
            Toast.makeText(context, "Не удалось войти", Toast.LENGTH_LONG).show()
        }
    }

    Spacer(modifier = Modifier.size(16.dp))
    Column() {
        InputField(
            value = inputLogin,
            onValueChange = {
                inputLogin = it
                viewModel.onIntent(AuthIntent.TextInput(inputLogin, inputPassword))
            },
            placeholder = "Почта",
            containerColor = containerColor,
            textColor = textColor,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))

        InputField(
            value = inputPassword,
            onValueChange = {
                inputPassword = it
                viewModel.onIntent(AuthIntent.TextInput(inputLogin, inputPassword))
            },
            placeholder = "Пароль",
            containerColor = containerColor,
            isPassword = true,
            textColor = textColor,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
        )


        Spacer(modifier = Modifier.size(40.dp))
        Button(
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            onClick = {
                viewModel.onIntent(AuthIntent.Send(inputLogin, inputPassword))
            },
            enabled = inputLogin.isNotEmpty() && inputPassword.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = SineyIney,
                disabledContainerColor = SineyIney.copy(alpha = 0.5f),
            ),
        ) {
            Text(
                text = "Войти",
                color = buttonTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = CustomTypography.bodyMedium
            )
        }



        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
        ) {
            Text(
                text = "Нет аккаунта?",
                fontSize = 16.sp,
                color = Black,
                fontFamily = FontFamily.Default,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Зарегистрироваться",
                fontSize = 16.sp,
                color = SineyIney,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Default,
                modifier = Modifier.clickable {
                    navController.navigate(RegisterRoute)
                }
            )
        }
    }

//    if (state.error != null) {
//        Text(
//            modifier = Modifier,
//            text = state.error,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color.Red,
//        )
//    }
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