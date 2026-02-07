package ru.sicampus.bootcamp2026.android.ui.testScreens.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.android.data.SignUpRepository
import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.SignUpNetworkDataSource
import ru.sicampus.bootcamp2026.android.domain.signUp.CheckSignUpFormatUseCase
import ru.sicampus.bootcamp2026.android.domain.signUp.RegisterAndSaveAuthUseCase
import ru.sicampus.bootcamp2026.android.ui.nav.HomeRoute

class SignUpViewModel : ViewModel() {
    private val checkSignUpFormatUseCase by lazy { CheckSignUpFormatUseCase() }
    private val registerAndSaveAuthUseCase by lazy {
        RegisterAndSaveAuthUseCase(
            SignUpRepository(
                signUpNetworkDataSource = SignUpNetworkDataSource(),
                authLocalDataSource = AuthLocalDataSource
            )
        )
    }

    private val _uiState = MutableStateFlow<SignUpState>(
        SignUpState.Data(
            isEnabledSend = false,
            error = null,
            validationHint = null
        )
    )
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    private val _actionFlow = MutableSharedFlow<SignUpAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    fun onIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.Send -> {
                // КРИТИЧНО: Дополнительная проверка перед отправкой
                val isValid = checkSignUpFormatUseCase.invoke(
                    fullName = intent.fullName,
                    department = intent.department,
                    position = intent.position,
                    email = intent.email,
                    password = intent.password,
                    confirmPassword = intent.confirmPassword
                )

                if (!isValid) {
                    // Если валидация не прошла, показываем ошибку
                    updateStateIfData { oldState ->
                        oldState.copy(
                            error = getValidationError(
                                intent.fullName,
                                intent.department,
                                intent.position,
                                intent.email,
                                intent.password,
                                intent.confirmPassword
                            )
                        )
                    }
                    return
                }

                _uiState.value = SignUpState.Loading
                viewModelScope.launch {
                    registerAndSaveAuthUseCase.invoke(
                        email = intent.email.trim(),
                        password = intent.password,
                        fullName = intent.fullName.trim(),
                        department = intent.department.trim(),
                        position = intent.position.trim()
                    ).fold(
                        onSuccess = {
                            _actionFlow.emit(
                                SignUpAction.OpenScreen(HomeRoute)
                            )
                        },
                        onFailure = { error ->
                            updateStateIfData { oldState ->
                                oldState.copy(
                                    error = error.message ?: "Ошибка регистрации"
                                )
                            }
                        }
                    )
                }
            }

            is SignUpIntent.TextInput -> {
                val isValid = checkSignUpFormatUseCase.invoke(
                    fullName = intent.fullName,
                    department = intent.department,
                    position = intent.position,
                    email = intent.email,
                    password = intent.password,
                    confirmPassword = intent.confirmPassword
                )

                updateStateIfData { oldState ->
                    oldState.copy(
                        isEnabledSend = isValid,
                        error = null,
                        validationHint = if (!isValid) {
                            getValidationHint(
                                intent.fullName,
                                intent.department,
                                intent.position,
                                intent.email,
                                intent.password,
                                intent.confirmPassword
                            )
                        } else null
                    )
                }
            }

            is SignUpIntent.NavigateBack -> {
                viewModelScope.launch {
                    _actionFlow.emit(SignUpAction.NavigateBack)
                }
            }
        }
    }

    // Подсказка для live feedback (мягкая форма)
    private fun getValidationHint(
        fullName: String,
        department: String,
        position: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        return when {
            fullName.trim().isBlank() -> "Заполните ФИО"
            fullName.trim().length < 2 -> "ФИО: минимум 2 символа"
            department.trim().isBlank() -> "Заполните отдел"
            department.trim().length < 2 -> "Отдел: минимум 2 символа"
            position.trim().isBlank() -> "Заполните должность"
            position.trim().length < 2 -> "Должность: минимум 2 символа"
            email.trim().isBlank() -> "Заполните email"
            !email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()) -> "Некорректный формат email"
            password.isBlank() -> "Придумайте пароль"
            password.length < 6 -> "Пароль: минимум 6 символов"
            confirmPassword.isBlank() -> "Подтвердите пароль"
            password != confirmPassword -> "Пароли не совпадают"
            else -> null
        }
    }

    // Ошибка после попытки отправки (строгая форма)
    private fun getValidationError(
        fullName: String,
        department: String,
        position: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String {
        return when {
            fullName.trim().isBlank() -> "Ошибка: заполните поле ФИО"
            department.trim().isBlank() -> "Ошибка: заполните поле Отдел"
            position.trim().isBlank() -> "Ошибка: заполните поле Должность"
            email.trim().isBlank() -> "Ошибка: заполните поле Email"
            password.isBlank() -> "Ошибка: заполните поле Пароль"
            confirmPassword.isBlank() -> "Ошибка: подтвердите пароль"
            fullName.trim().length < 2 -> "ФИО должно содержать минимум 2 символа"
            department.trim().length < 2 -> "Отдел должен содержать минимум 2 символа"
            position.trim().length < 2 -> "Должность должна содержать минимум 2 символа"
            !email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()) -> "Некорректный формат email"
            password.length < 6 -> "Пароль должен содержать минимум 6 символов"
            password != confirmPassword -> "Пароли не совпадают"
            else -> "Проверьте правильность заполнения полей"
        }
    }

    private fun updateStateIfData(lambda: (SignUpState.Data) -> SignUpState) {
        _uiState.update { state ->
            (state as? SignUpState.Data)?.let { lambda.invoke(it) } ?: state
        }
    }
}