package ru.sicampus.bootcamp2026.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.ui.theme.*



@Composable
fun RegistrationScreen(navController: NavController, onRegisterSuccess: () -> Unit, onLoginClick: () -> Unit) {

    val viewModel: RegisterViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is RegisterState.Content) {
            val userEntity = (state as RegisterState.Content).user

            val userDto = UserDto(
                id = userEntity.id,
                email = userEntity.email,
                fullName = userEntity.fullName
            )

            AuthLocalDataSource.saveUser(userDto)
            onRegisterSuccess()
        }
    }

    when (val currentState = state) {
        RegisterState.Initial -> {
            RegisterContentState(
                name = name,
                onNameChange = { name = it },
                surname = surname,
                onSurnameChange = { surname = it },
                patronymic = patronymic,
                onPatronymicChange = { patronymic = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                onRegisterClick = {
                    if (password != confirmPassword) {
                        // TODO
                        return@RegisterContentState
                    }
                    val fullName = "$surname $name $patronymic".trim()
                    viewModel.register(email, password, fullName)
                },
                onLoginClick = { onLoginClick() }
            )
        }

        RegisterState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        is RegisterState.Error -> {
            RegisterErrorState(
                errorMessage = currentState.reason,
                onRetry = {
                    val fullName = "$surname $name $patronymic".trim()
                    viewModel.register(email, password, fullName)
                },
                onBackToForm = { viewModel.resetState() }
            )
        }

        is RegisterState.Content -> { RegisterContentState(userName = currentState.user.fullName) }
    }
}

@Composable
fun RegisterErrorState(
    errorMessage: String,
    onRetry: () -> Unit,
    onBackToForm: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = errorMessage, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
            //TODO
            Button(
                onClick = onRetry, modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Попробовать снова")
            }
            TextButton(onClick = onBackToForm) {
                Text("Вернуться к форме")
            }
        }
    }
}

@Composable
fun RegisterContentState(
    userName: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Добро пожаловать, $userName!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Composable
private fun RegisterContentState(
    name: String,
    onNameChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    patronymic: String,
    onPatronymicChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
    ){

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(start = 32.dp, end = 32.dp, top = 96.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(56.dp)
        ) {
            // Заголовок "Регистрация"
            Text(
                text = "Регистрация",
                fontSize = 24.sp,
                color = Black,
                fontFamily = Montserrat,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    //  (Имя, Фамилия, Отчество, Почта)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        InputField(
                            value = name,
                            onValueChange = onNameChange,
                            placeholder = "Имя",
                            containerColor = containerColor,
                            textColor = textColor,
                            modifier = Modifier.fillMaxWidth().height(51.dp)
                        )

                        InputField(
                            value = surname,
                            onValueChange = onSurnameChange,
                            placeholder = "Фамилия",
                            containerColor = containerColor,
                            textColor = textColor,
                            modifier = Modifier.fillMaxWidth().height(51.dp)
                        )

                        InputField(
                            value = patronymic,
                            onValueChange = onPatronymicChange,
                            placeholder = "Отчество",
                            containerColor = containerColor,
                            textColor = textColor,
                            modifier = Modifier.fillMaxWidth().height(51.dp)
                        )

                        InputField(
                            value = email,
                            onValueChange =onEmailChange,
                            placeholder = "Почта",
                            containerColor = containerColor,
                            textColor = textColor,
                            modifier = Modifier.fillMaxWidth().height(51.dp)
                        )
                    }

                    // (Пароль и подтверждение пароля)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Пароль
                        InputField(
                            value = password,
                            onValueChange = onPasswordChange,
                            placeholder = "Пароль",
                            containerColor = containerColor,
                            textColor = textColor,
                            isPassword = true,
                            modifier = Modifier.fillMaxWidth().height(51.dp)
                        )

                        // Подтверждение пароля
                        InputField(
                            value = confirmPassword,
                            onValueChange = onConfirmPasswordChange,
                            placeholder = "Повторите пароль",
                            containerColor = containerColor,
                            textColor = textColor,
                            isPassword = true,
                            modifier = Modifier.fillMaxWidth().height(51.dp)
                        )
                    }
                }

                // (кнопка и чекбокс)
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    // Кнопка регистрации
                    Box(
                        modifier = Modifier.fillMaxWidth().height(51.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(SineyIney)
                            .clickable {
                                onRegisterClick()
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Зарегистрироваться",
                            color = buttonTextColor,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                        )
                    }

                    // текст с ссылкой на вход
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Уже есть аккаунт?", color = Black, style = MaterialTheme.typography.bodyMedium,)

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Войти", style = MaterialTheme.typography.bodyMedium, color = SineyIney,
                            modifier = Modifier.clickable { onLoginClick() })
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    containerColor: Color,
    textColor: Color,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(30.dp)).background(containerColor)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = textColor,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                style = MaterialTheme.typography.displayMedium
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = if (value.isEmpty()) textColor else Color.Black,
                fontFamily = FontFamily.Default
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().height(51.dp)
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RegistrationScreenPreview() {
//    MaterialTheme(
//        typography = CustomTypography
//    ) {
//        RegistrationScreen()
//    }
//}