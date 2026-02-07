package ru.sicampus.bootcamp2026.ui.theme.screens.Login

import ru.sicampus.bootcamp2026.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.DeepBlue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.data.source.UserPreferences


@Composable
fun LoginScreen(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
){
    val viewModel: LoginViewModel = viewModel(
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(appViewModel,
                userPreferences = userPreferences) as T
        }
    }
    )
    val state by viewModel.uiState.collectAsState()
    when(val currentState = state){
        is LoginState.Error -> LoginError(currentState, onRefresh = {viewModel.getData()})
        is LoginState.Loading -> LoginLoading()
        is LoginState.Content -> LoginContent(onIntent = { intent -> viewModel.onIntent(intent)}, LoginClick = {viewModel.LoginClick()} )
        is LoginState.Reg -> RegistrationScreen(onRegistrationClick = {viewModel.onRegistrationClick()}, onIntent = {intent -> viewModel.onIntent(intent)} )
    }

}



@Composable
fun LoginLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun LoginError(
    state: LoginState.Error,
    onRefresh: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.reason)
                Button(
                    onClick = onRefresh
                ) {
                    Text("refresh")
                }
            }
        }
}

@Composable
private fun LoginContent(
    onIntent: (AuthIntent) -> Unit,
    LoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.login_wave4),
            contentDescription = "Wave_4",
            modifier = Modifier
                .width(524.dp)
                .height(221.dp)
                .align(Alignment.TopStart)
                .offset(x = 0.dp, y = (-50).dp)
        )

        Image(
            painter = painterResource(R.drawable.login_wave3),
            contentDescription = "Wave_3",
            modifier = Modifier
                .width(524.dp)
                .height(221.dp)
                .align(Alignment.TopStart)
                .offset(x = (20).dp, y = (-50).dp)
        )
        Image(
            painter = painterResource(R.drawable.login_wave2),
            contentDescription = "Wave_2",
            modifier = Modifier
                .width(596.55.dp)
                .height(477.68.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-10).dp, y = 60.dp)
        )

        Image(
            painter = painterResource(R.drawable.login_wave1),
            contentDescription = "Wave_1",
            modifier = Modifier
                .width(596.55.dp)
                .height(477.68.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 50.dp)
        )

        Surface(
            modifier = Modifier
                .width(368.dp)
                .height(450.dp)
                .align(Alignment.Center)
                .padding(6.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text("Вход", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(39.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    value = email,
                    shape = RoundedCornerShape(6.dp),
                    onValueChange = { email = it },
                    label = {
                        Text(
                            "Email",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    value = password,
                    onValueChange = { password = it},
                    shape = RoundedCornerShape(6.dp),
                    label = {
                        Text(
                            "Password",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Spacer(modifier = Modifier.height(39.dp))

                Button(
                    onClick = { onIntent(AuthIntent.Send(email, password = password)) },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Войти")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("Ещё нет аккаунта?")
                TextButton(onClick = { LoginClick() }) {
                    Text(
                        "Зарегистрироваться",
                        color = DeepBlue,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    _root_ide_package_.ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme {
    }
}