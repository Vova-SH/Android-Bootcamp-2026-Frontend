package ru.sicampus.bootcamp2026.android.ui.testScreens.signUp

import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.components.CustomButton
import ru.sicampus.bootcamp2026.android.ui.components.CustomTextField
import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.android.ui.theme.ErrorRed
import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
import ru.sicampus.bootcamp2026.android.ui.theme.White

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = viewModel(),
    navController: NavController,
) {
    SecureScreen()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is SignUpAction.OpenScreen -> navController.navigate(action.route)
                is SignUpAction.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow_icon),
            contentDescription = "Назад",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 46.dp, start = 24.dp)
                .clickable { viewModel.onIntent(SignUpIntent.NavigateBack) }
        )

        Spacer(modifier = Modifier.height(87.dp))

        Text(
            text = "Регистрация",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.open_sans_bold))
        )

        when (val currentState = state) {
            is SignUpState.Data -> Content(viewModel, currentState)
            is SignUpState.Loading -> {
                Spacer(modifier = Modifier.height(100.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Composable
private fun Content(
    viewModel: SignUpViewModel,
    state: SignUpState.Data
) {
    var fullName by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var createPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val focusDepartmentRequester = remember { FocusRequester() }
    val focusPositionRequester = remember { FocusRequester() }
    val focusEmailRequester = remember { FocusRequester() }
    val focusCreatePasswordRequester = remember { FocusRequester() }
    val focusConfirmPasswordRequester = remember { FocusRequester() }

    // Функция для обновления валидации
    fun updateValidation() {
        viewModel.onIntent(
            SignUpIntent.TextInput(
                fullName = fullName,
                department = department,
                position = position,
                email = email,
                password = createPassword,
                confirmPassword = confirmPassword
            )
        )
    }

    // Функция для отправки (только если валидация пройдена)
    fun trySendRegistration() {
        if (state.isEnabledSend) {
            viewModel.onIntent(
                SignUpIntent.Send(
                    fullName = fullName,
                    department = department,
                    position = position,
                    email = email,
                    password = createPassword,
                    confirmPassword = confirmPassword
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // ФИО
    CustomTextField(
        value = fullName,
        onValueChange = { newFullName ->
            fullName = newFullName
            updateValidation()
        },
        label = "ФИО",
        modifier = Modifier,
        textColor = TextGrey,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        keyboardActionOnNext = {
            focusDepartmentRequester.requestFocus()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Отдел
    CustomTextField(
        value = department,
        onValueChange = { newDepartment ->
            department = newDepartment
            updateValidation()
        },
        label = "Отдел",
        modifier = Modifier,
        textColor = TextGrey,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        focusRequester = focusDepartmentRequester,
        keyboardActionOnNext = {
            focusPositionRequester.requestFocus()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Должность
    CustomTextField(
        value = position,
        onValueChange = { newPosition ->
            position = newPosition
            updateValidation()
        },
        label = "Должность",
        modifier = Modifier,
        textColor = TextGrey,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        focusRequester = focusPositionRequester,
        keyboardActionOnNext = {
            focusEmailRequester.requestFocus()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Электронная почта
    CustomTextField(
        value = email,
        onValueChange = { newEmail ->
            email = newEmail
            updateValidation()
        },
        label = "Электронная почта",
        modifier = Modifier,
        textColor = TextGrey,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        focusRequester = focusEmailRequester,
        keyboardActionOnNext = {
            focusCreatePasswordRequester.requestFocus()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Придумайте пароль
    CustomTextField(
        value = createPassword,
        onValueChange = { newPassword ->
            createPassword = newPassword
            updateValidation()
        },
        label = "Придумайте пароль",
        modifier = Modifier,
        textColor = TextGrey,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Next,
        visualTransformation = PasswordVisualTransformation(),
        focusRequester = focusCreatePasswordRequester,
        keyboardActionOnNext = {
            focusConfirmPasswordRequester.requestFocus()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Подтвердите пароль
    CustomTextField(
        value = confirmPassword,
        onValueChange = { newConfirmPassword ->
            confirmPassword = newConfirmPassword
            updateValidation()
        },
        label = "Подтвердите пароль",
        modifier = Modifier,
        textColor = TextGrey,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        visualTransformation = PasswordVisualTransformation(),
        focusRequester = focusConfirmPasswordRequester,
        keyboardActionOnDone = {
            // КРИТИЧНО: проверяем валидацию перед отправкой
            trySendRegistration()
        }
    )

    Spacer(modifier = Modifier.height(24.dp))

    // LIVE VALIDATION FEEDBACK - показываем текущие проблемы валидации
    if (state.validationHint != null && !state.isEnabledSend) {
        Box(
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .border(
                    width = 1.dp,
                    color = TextGrey,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 13.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.validationHint,
                color = TextGrey,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    // Кнопка регистрации - используем стандартный Button для гарантированной работы enabled
    Button(
        onClick = {
            trySendRegistration()
        },
        modifier = Modifier
            .padding(start = 64.dp, end = 64.dp)
            .fillMaxWidth()
            .height(48.dp),
        enabled = state.isEnabledSend,
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkBlue,
            contentColor = White,
            disabledContainerColor = DarkBlue.copy(alpha = 0.5f),
            disabledContentColor = White.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = "Зарегистрироваться",
            fontWeight = FontWeight.Bold
        )
    }

    // Показываем ошибку после попытки отправки
    if (state.error != null) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .border(
                    width = 2.dp,
                    color = ErrorRed,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 13.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.error,
                color = ErrorRed,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
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