package ru.sicampus.bootcamp2026.ui.screen.register

import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    val context = LocalContext.current;

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
                    viewModel.validateAndRegister(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        name = name,
                        surname = surname,
                        patronymic = patronymic,
                        onValidationError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        },
                        onSuccess = {
                            val fullName = "$surname $name $patronymic".trim()
                            viewModel.register(email, password, fullName)
                        }
                    )

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
            Text(text = errorMessage,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                style = MaterialTheme.typography.displayMedium
            )
            Button(
                onClick = onBackToForm, modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Вернуться к форме")
            }
//            TextButton(onClick = onBackToForm) {
//                Text("Вернуться к форме")
//            }
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

    val allFieldsFilled = remember(
        name,
        surname,
        patronymic,
        email,
        password,
        confirmPassword
    ) {
        name.isNotEmpty() &&
                surname.isNotEmpty() &&
                patronymic.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, top = 96.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(56.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(40.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // (Имя, Фамилия, Отчество, Почта)
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                            onValueChange = onEmailChange,
                            placeholder = "Email",
                            containerColor = containerColor,
                            textColor = textColor,
                            modifier = Modifier.fillMaxWidth().height(51.dp)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(vertical = 16.dp)
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

                // (кнопка и ссылка на вход)
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(51.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(
                                if (allFieldsFilled) SineyIney else SineyIney.copy(alpha = 0.5f)
                            )
                            .clickable(
                                enabled = allFieldsFilled,
                                onClick = onRegisterClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Создать аккаунт",
                            color = buttonTextColor,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Текст с ссылкой на вход
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Уже есть аккаунт?",
                            color = Black,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Войти",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SineyIney,
                            modifier = Modifier.clickable { onLoginClick() }
                        )
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
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(containerColor)
            .heightIn(min = 56.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = if (value.isEmpty()) textColor else Color.Black,
                fontFamily = FontFamily.Default
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.displayMedium
            )
        }
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