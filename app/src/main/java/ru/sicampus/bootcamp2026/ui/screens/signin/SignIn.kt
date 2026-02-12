package ru.sicampus.bootcamp2026.ui.screens.signin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.ui.theme.accentBlue

@Composable
fun SignIn (
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel<SignInViewModel>(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    /*здесь происходят перемещения. каждый раз, когда
    * actionFlow из SignInViewModel меняется, происходит
    * навигация */
    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is SignInAction.OpenScreen -> navController.navigate(action.route)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Row (){
        Text("Вход", fontSize = 30.sp)
            Spacer(modifier = Modifier.fillMaxWidth())
        }*/
        Text("Вход", fontSize = 30.sp)
        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        /* state может не содержать данных, поскольку они ещё не подгрузились */
        when (val currentState = state) {
            is SignInState.Data -> Content(viewModel, currentState)
            is SignInState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}
    @Composable
    private fun Content(
        viewModel: SignInViewModel,
        state: SignInState.Data
    ) {
        val login = remember { mutableStateOf("") }
        val pass = remember { mutableStateOf("") }
        OutlinedTextField(
            label = { Text("Логин") },
            value = login.value,
            onValueChange = { it ->
                login.value = it
                //обновление state. необходимо для блокировки и разблокировки кнопки "Вход"
                viewModel.onIntent(SignInIntent.TextInput(login.value, pass.value))
            },
            shape = RoundedCornerShape(20.dp)
        )
        OutlinedTextField(
            label = { Text("Пароль") },
            value = pass.value,
            onValueChange = {
                pass.value = it
                viewModel.onIntent(SignInIntent.TextInput(login.value, pass.value))
            },
            shape = RoundedCornerShape(20.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(
            onClick = {
                viewModel.onIntent(SignInIntent.Send(login.value, pass.value))
            },
            colors = ButtonDefaults.buttonColors(
                accentBlue
            ),
            modifier = Modifier.width(275.dp),
            shape = RoundedCornerShape(15.dp),
            enabled = state.isEnabledSend
        ) {
            Text("Вход")
        }
        if (state.error != null) {
            Text(
                text = state.error,
            )
        }
        OutlinedButton(
            //по сути здесь просто написано navController.navigate(SignUp) :)
            onClick = {
                viewModel.onIntent(SignInIntent.Register)
            },
            border = BorderStroke(1.dp, accentBlue),
            modifier = Modifier.width(275.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Регистрация")
        }
        /*OtlinedButton(onClick = {},
            colors = ButtonDefaults.buttonColors(
                transporant
            ),
            border = BorderStroke(1.dp, accentBlue),
            modifier = Modifier.padding(2.dp).width(275.dp)
        ){
            Text("Регистрация")
        }*/
    }

/*
@Preview
@Composable
fun see() {
    SignIn(onNavigateToSignUp = {
        navController.navigate(route = SignUp)
    })
}*/
