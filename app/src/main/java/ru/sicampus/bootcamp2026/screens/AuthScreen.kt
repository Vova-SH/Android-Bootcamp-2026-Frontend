package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.PasswordTextField
import ru.sicampus.bootcamp2026.components.PrimaryButton
import ru.sicampus.bootcamp2026.components.PrimaryTextField
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.data.repo.BootcampRepository
import ru.sicampus.bootcamp2026.data.util.toUiMessage
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.White

enum class AuthMode { Login, Register }

@Composable
fun AuthScreen(
    startMode: AuthMode,
    onAuthSuccess: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val repo = remember { BootcampRepository() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var mode by remember { mutableStateOf(startMode) }
    LaunchedEffect(startMode) { mode = startMode }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("TEST") }
    var passwordRepeat by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    fun showWarn(msg: String) {
        errorText = msg
        scope.launch { snackbarHostState.showSnackbar(msg) }
    }

    fun validate(): String? {
        if (login.isBlank()) return "Введите логин"
        if (password.isBlank()) return "Введите пароль"
        if (mode == AuthMode.Register) {
            if (name.isBlank()) return "Введите имя"
            if (lastname.isBlank()) return "Введите фамилию"
            if (position.isBlank()) return "Введите должность"
            if (passwordRepeat.isBlank()) return "Повторите пароль"
            if (password != passwordRepeat) return "Пароли не совпадают"
        }
        return null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.52f)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 56.dp,
                        bottomEnd = 56.dp
                    )
                )
                .background(Gray)
                .padding(start = 24.dp, end = 24.dp, top = 72.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Column {
                    Text(
                        text = if (mode == AuthMode.Login) "Вход" else "Регистрация",
                        style = MaterialTheme.typography.headlineLarge,
                        color = White
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Быстро назначай. Легко согласуй.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(216.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // На небольших экранах форма может не помещаться — даём скролл.
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.48f)
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .imePadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Переключатель режимов
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { mode = AuthMode.Register; errorText = null }) {
                    Text(
                        text = "Регистрация",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (mode == AuthMode.Register) TextDecoration.Underline else TextDecoration.None,
                        color = White
                    )
                }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = { mode = AuthMode.Login; errorText = null }) {
                    Text(
                        text = "Вход",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (mode == AuthMode.Login) TextDecoration.Underline else TextDecoration.None,
                        color = White
                    )
                }

                Spacer(Modifier.weight(1f))

                TextButton(onClick = onBack) {
                    Text(
                        text = "Назад",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White
                    )
                }
            }

            if (mode == AuthMode.Register) {
                PrimaryTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Имя...",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                PrimaryTextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    placeholder = "Фамилия...",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                PrimaryTextField(
                    value = position,
                    onValueChange = { position = it },
                    placeholder = "Должность (например, Junior Android Developer)...",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }

            PrimaryTextField(
                value = login,
                onValueChange = { login = it },
                placeholder = "Email/логин...",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Пароль...",
                modifier = Modifier.fillMaxWidth()
            )

            if (mode == AuthMode.Register) {
                Spacer(Modifier.height(12.dp))
                PasswordTextField(
                    value = passwordRepeat,
                    onValueChange = { passwordRepeat = it },
                    placeholder = "Повтор пароля...",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            PrimaryButton(
                text = if (mode == AuthMode.Login) "Войти" else "Регистрация",
                enabled = !isLoading,
                onClick = {
                    errorText = null
                    val v = validate()
                    if (v != null) {
                        showWarn(v)
                        return@PrimaryButton
                    }

                    isLoading = true
                    scope.launch {
                        runCatching {
                            if (mode == AuthMode.Register) {
                                repo.register(
                                    UserRegisterDto(
                                        name = name.trim(),
                                        lastname = lastname.trim(),
                                        password = password,
                                        login = login.trim(),
                                        position = position.trim()
                                    )
                                )
                            }
                            // После регистрации делаем login, чтобы backend выдал данные пользователя
                            // и чтобы все дальнейшие запросы были авторизованы.
                            repo.login(login.trim(), password)
                        }
                            .onSuccess {
                                isLoading = false
                                onAuthSuccess()
                            }
                            .onFailure {
                                isLoading = false
                                showWarn(it.toUiMessage())
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (!errorText.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = errorText!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.weight(1f))

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}
