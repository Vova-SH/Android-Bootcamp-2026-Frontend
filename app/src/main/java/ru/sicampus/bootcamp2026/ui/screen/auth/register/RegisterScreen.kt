package ru.sicampus.bootcamp2026.ui.screen.auth.register

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.AppTopBar
import ru.sicampus.bootcamp2026.components.AuthFooter
import ru.sicampus.bootcamp2026.components.InputField
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    navController: NavController,
) {
    val state by viewModel.uiState.collectAsState()

    when (val currentState = state) {
        is RegisterState.Data -> Content(viewModel, currentState, navController)
        is RegisterState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp)
            )
        }
        RegisterState.Success -> Unit
    }
}


@Composable
private fun Content(
    viewModel: RegisterViewModel,
    state: RegisterState.Data,
    navController: NavController
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
                title = "Регистрация",
                startIconId =  R.drawable.ic_back,
                startIconButtonOnClick = {}
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {

                val fields by viewModel.fields.collectAsState()

                Spacer(modifier = Modifier.height(112.dp))

                InputField(
                    title = "ФИО",
                    value = fields.name,
                    onValueChange = {
                        viewModel.onIntent(RegisterIntent.FieldChanged(fields.copy(name = it)))
                    },
                    placeholderText = "Фамилия Имя Отчество",
                    onFocusChanged = {
                        viewModel.onFieldFocusChanged("name", it)
                    },
                    error = state.fieldErrors["name"]
                )

                InputField(
                    title = "Логин",
                    value = fields.username,
                    onValueChange = {
                        viewModel.onIntent(RegisterIntent.FieldChanged(fields.copy(username = it)))
                    },
                    placeholderText = "Придумайте логин",
                    onFocusChanged = {
                        viewModel.onFieldFocusChanged("username", it)
                    },
                    error = state.fieldErrors["username"]
                )

                InputField(
                    title = "Пароль",
                    value = fields.password,
                    onValueChange = {
                        viewModel.onIntent(RegisterIntent.FieldChanged(fields.copy(password = it)))
                    },
                    placeholderText = "Придумайте пароль",
                    onFocusChanged = {
                        viewModel.onFieldFocusChanged("password", it)
                    },
                    error = state.fieldErrors["password"],
                    isPassword = true
                )

                InputField(
                    title = "Должность",
                    value = fields.position,
                    onValueChange = {
                        viewModel.onIntent(RegisterIntent.FieldChanged(fields.copy(position = it)))
                    },
                    placeholderText = "Укажите должность",
                    onFocusChanged = {
                        viewModel.onFieldFocusChanged("position", it)
                    },
                    error = state.fieldErrors["position"]
                )

                InputField(
                    title = "Почта",
                    value = fields.email,
                    onValueChange = {
                        viewModel.onIntent(RegisterIntent.FieldChanged(fields.copy(email = it)))
                    },
                    placeholderText = "example@mail.ru",
                    keyboardType = KeyboardType.Email,
                    onFocusChanged = {
                        viewModel.onFieldFocusChanged("email", it)
                    },
                    error = state.fieldErrors["email"]
                )

                InputField(
                    title = "Телефон",
                    value = fields.phoneNumber,
                    onValueChange = {
                        viewModel.onIntent(RegisterIntent.FieldChanged(fields.copy(phoneNumber = it)))
                    },
                    placeholderText = "+7 (___) ___-__-__",
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onIntent(RegisterIntent.Submit)
                        }
                    ),
                    onFocusChanged = {
                        viewModel.onFieldFocusChanged("phoneNumber", it)
                    },
                    error = state.fieldErrors["phoneNumber"]
                )

                Spacer(modifier = Modifier.height(172.dp))
            }


            AuthFooter (
                modifier = Modifier.align(Alignment.BottomCenter),
                primaryButtonText = "Зарегистрироваться",
                primaryButtonOnClick = { viewModel.onIntent(RegisterIntent.Submit) },
                buttonEnabled = state.isEnabledSend,
                secondaryText1 = "Есть аккаунт? ",
                secondaryText2 = "Войти",
                secondaryText2OnClick = { navController.popBackStack() }
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