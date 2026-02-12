package ru.sicampus.bootcamp2026.ui.screens.auth.signup

import android.content.Context
import android.content.Intent
import android.util.Patterns
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.login.nav.LoginScreenDestination
import ru.sicampus.bootcamp2026.ui.root.RootActivity
import ru.sicampus.bootcamp2026.ui.root.theme.BlueMain
import ru.sicampus.bootcamp2026.ui.screens.auth.login.CustomEmailTextField
import ru.sicampus.bootcamp2026.ui.screens.auth.login.CustomPasswordTextField
import ru.sicampus.bootcamp2026.ui.screens.auth.login.ValidationState
import ru.sicampus.bootcamp2026.utils.SettingsUtils


@Composable
fun SignUpScreen(
    context: Context,
    navController: NavHostController,
    viewModel: SignUpViewModel = viewModel(factory = SignUpViewModelFactory.create(context))
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { actionState ->
            when (actionState) {
                is ActionState.LoginScreen -> {
                    navController.navigate(LoginScreenDestination) {
                        popUpTo(LoginScreenDestination) { inclusive = true }
                    }
                }
            }
        }
    }

    val nameValidationState = remember(uiState.name) {
        derivedStateOf {
            when {
                uiState.name.isBlank() -> ValidationState.Empty
                uiState.name.length > 20 -> ValidationState.Error("Имя должно быть не более 20 символов")
                else -> ValidationState.Valid
            }
        }
    }

    val surnameValidationState = remember(uiState.surname) {
        derivedStateOf {
            when {
                uiState.surname.isBlank() -> ValidationState.Empty
                uiState.surname.length > 20 -> ValidationState.Error("Фамилия должна быть не более 20 символов")
                else -> ValidationState.Valid
            }
        }
    }

    val emailValidationState = remember(uiState.email) {
        derivedStateOf {
            when {
                uiState.email.isBlank() -> ValidationState.Empty
                uiState.email.length > 255 -> ValidationState.Error("Слишком длинный email")
                !Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches() ->
                    ValidationState.Error("Неверный формат email")
                else -> ValidationState.Valid
            }
        }
    }
    val passwordValidationState = remember(uiState.password) {
        derivedStateOf {
            when {
                uiState.password.isBlank() -> ValidationState.Empty
                uiState.password.length < 8 -> ValidationState.Error("Пароль должен быть не менее 8 символов")
                uiState.password.length > 64 -> ValidationState.Error("Пароль должен быть не более 64 символов")
                else -> ValidationState.Valid
            }
        }
    }
    val password2ValidationState = remember(uiState.password2, uiState.password) {
        derivedStateOf {
            when {
                uiState.password2.isBlank() -> ValidationState.Empty
                uiState.password2 != uiState.password -> ValidationState.Error("Пароли не совпадают")
                else -> ValidationState.Valid
            }
        }
    }
    val isFormValid by remember {
        derivedStateOf {
            nameValidationState.value is ValidationState.Valid &&
                    surnameValidationState.value is ValidationState.Valid &&
                    emailValidationState.value is ValidationState.Valid &&
                    passwordValidationState.value is ValidationState.Valid &&
                    password2ValidationState.value is ValidationState.Valid
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.isSignUpSuccess) {
            context.startActivity(
                Intent(context, RootActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )

            viewModel.resetSignUpState()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(46.dp)
                )
                .width(380.dp)
                .clip(RoundedCornerShape(44.dp))
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.size(26.dp))
                Icon(
                    painterResource(R.drawable.app_icon),
                    "",
                    modifier = Modifier.size(90.dp),
                    tint = Color(0xff155DFC)
                )
                Text(
                    stringResource(R.string.app_name),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    stringResource(R.string.app_name_desk),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.alpha(0.7f)
                )
                Spacer(Modifier.size(36.dp))

                CustomNameTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    placeholder = stringResource(R.string.person_name),
                    validatorHasErrors = nameValidationState.value is ValidationState.Error,
                    errorMessage = if (nameValidationState.value is ValidationState.Error) {
                        (nameValidationState.value as ValidationState.Error).message
                    } else null
                )

                Spacer(Modifier.size(20.dp))

                CustomNameTextField(
                    value = uiState.surname,
                    onValueChange = { viewModel.onSurnameChange(it) },
                    placeholder = stringResource(R.string.person_surname),
                    validatorHasErrors = surnameValidationState.value is ValidationState.Error,
                    errorMessage = if (surnameValidationState.value is ValidationState.Error) {
                        (surnameValidationState.value as ValidationState.Error).message
                    } else null
                )

                Spacer(Modifier.size(20.dp))

                CustomEmailTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = stringResource(R.string.email),
                    validatorHasErrors = emailValidationState.value is ValidationState.Error,
                    errorMessage = if (emailValidationState.value is ValidationState.Error) {
                        (emailValidationState.value as ValidationState.Error).message
                    } else null
                )

                Spacer(Modifier.size(20.dp))

                CustomPasswordTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = stringResource(R.string.password),
                    validatorPasswordHasErrors = passwordValidationState.value is ValidationState.Error,
                    errorMessage = if (passwordValidationState.value is ValidationState.Error) {
                        (passwordValidationState.value as ValidationState.Error).message
                    } else null
                )

                Spacer(Modifier.size(20.dp))
                CustomPasswordTextField(
                    value = uiState.password2,
                    onValueChange = { viewModel.onPassword2Change(it) },
                    placeholder = stringResource(R.string.repeat_password),
                    validatorPasswordHasErrors = password2ValidationState.value is ValidationState.Error,
                    errorMessage = if (password2ValidationState.value is ValidationState.Error) {
                        (password2ValidationState.value as ValidationState.Error).message
                    } else null
                )

                Spacer(Modifier.size(20.dp))

                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(0.9f)
                    )
                }
                Button(
                    onClick = {
                        if (isFormValid) viewModel.register()
                    },
                    modifier = Modifier
                        .height(53.dp)
                        .fillMaxWidth(0.9f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff155DFC)),
                    enabled = isFormValid && !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Text(
                            stringResource(R.string.signup),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(Modifier.size(14.dp))

                Text(
                    text = "Войти",
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { navController.navigate(LoginScreenDestination) }
                    ),
                    color = BlueMain,
                    fontSize = 16.sp
                )

                Spacer(Modifier.size(26.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    validatorHasErrors: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 8.dp else 2.dp,
        animationSpec = tween(durationMillis = 200)
    )

    Column{
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = when {
                        validatorHasErrors -> Color.Red.copy(alpha = 0.6f)
                        isFocused -> Color(0xff155DFC).copy(alpha = 0.6f)
                        else -> Color.LightGray.copy(alpha = 0.3f)
                    },
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = if (enabled) Color.White else Color.LightGray
                )
                .clickable(
                    enabled = enabled,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusRequester.requestFocus() },
            color = Color.Transparent
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.width(12.dp))

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    enabled = enabled,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    textStyle = TextStyle(
                        color = if (enabled) Color.Black else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = Color.Gray.copy(alpha = 0.6f),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        if (validatorHasErrors && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

