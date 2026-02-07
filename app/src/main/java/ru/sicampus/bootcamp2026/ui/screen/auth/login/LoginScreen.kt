package ru.sicampus.bootcamp2026.ui.screen.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.components.AppTopBar
import ru.sicampus.bootcamp2026.components.AuthFooter
import ru.sicampus.bootcamp2026.components.InputField
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    navController: NavController,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect{action ->
            when(action) {
                is LoginAction.OpenScreen -> navController.navigate(action.route)
            }

        }
    }

    when (val currentState = state) {
        is LoginState.Data -> Content(viewModel, currentState, onNavigateToRegister)
        is LoginState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp)
            )
        }
    }
}


@Composable
private fun Content(
    viewModel: LoginViewModel,
    state: LoginState.Data,
    onNavigateToRegister: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val minHeightForIcon = 600.dp

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        if (screenHeightDp > minHeightForIcon) {
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
                    contentDescription = null,
                    Modifier.size(100.dp),
                    tint = White
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(White)
        ) {

            AppTopBar(
                title = "Вход",
                startIconId =  R.drawable.ic_back,
                startIconButtonOnClick = {}
            )


            var inputLogin by remember { mutableStateOf("") }
            var inputPassword by remember { mutableStateOf("") }
            val focusPasswordRequester = remember { FocusRequester() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Spacer(modifier = Modifier.height(112.dp))

                InputField(
                    title = "Логин",
                    value = inputLogin,
                    onValueChange = {
                        inputLogin = it
                        viewModel.onIntent(LoginIntent.TextInput(inputLogin, inputPassword))
                    },
                    placeholderText = "Введите логин",
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusPasswordRequester.requestFocus()
                        }
                    )
                )

                InputField(
                    title = "Пароль",
                    value = inputPassword,
                    onValueChange = {
                        inputPassword = it
                        viewModel.onIntent(LoginIntent.TextInput(inputLogin, inputPassword))
                    },
                    placeholderText = "Введите пароль",
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    modifier = Modifier.focusRequester(focusPasswordRequester).fillMaxWidth(),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onIntent(LoginIntent.Send(inputLogin, inputPassword))
                        }
                    ),
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(172.dp))
            }

            AuthFooter(
                modifier = Modifier.align(Alignment.BottomCenter),
                primaryButtonText = "Войти",
                primaryButtonOnClick = {
                    viewModel.onIntent(LoginIntent.Send(inputLogin, inputPassword))
                },
                buttonEnabled = state.isEnabledSend,
                secondaryText1 = "Нет аккаунта? ",
                secondaryText2 = "Создать",
                secondaryText2OnClick = onNavigateToRegister
            )

            if (state.error != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = state.error,
                    fontSize = 24.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}