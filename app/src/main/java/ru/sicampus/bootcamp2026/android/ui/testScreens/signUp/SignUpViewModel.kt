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
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.R


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
                val isValid = checkSignUpFormatUseCase.invoke(
                    fullName = intent.fullName,
                    department = intent.department,
                    position = intent.position,
                    email = intent.email,
                    password = intent.password,
                    confirmPassword = intent.confirmPassword
                )

                if (!isValid) {
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
                                    error = error.message ?: App.context.getString(R.string.su_error_registration)
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

    private fun getValidationHint(
        fullName: String,
        department: String,
        position: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        return when {
            fullName.trim().isBlank() -> App.context.getString(R.string.su_hint_fill_fullname)
            fullName.trim().length < 2 -> App.context.getString(R.string.su_hint_fullname_min_2)
            department.trim().isBlank() -> App.context.getString(R.string.su_hint_fill_department)
            department.trim().length < 2 -> App.context.getString(R.string.su_hint_department_min_2)
            position.trim().isBlank() -> App.context.getString(R.string.su_hint_fill_position)
            position.trim().length < 2 -> App.context.getString(R.string.su_hint_position_min_2)
            email.trim().isBlank() -> App.context.getString(R.string.su_hint_fill_email)
            !email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()) ->
                App.context.getString(R.string.su_hint_email_invalid)
            password.isBlank() -> App.context.getString(R.string.su_hint_password_create)
            password.length < 6 -> App.context.getString(R.string.su_hint_password_min_6)
            confirmPassword.isBlank() -> App.context.getString(R.string.su_hint_password_confirm)
            password != confirmPassword -> App.context.getString(R.string.su_hint_passwords_not_match)
            else -> null
        }
    }

    private fun getValidationError(
        fullName: String,
        department: String,
        position: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String {
        return when {
            fullName.trim().isBlank() -> App.context.getString(R.string.su_error_fill_fullname)
            department.trim().isBlank() -> App.context.getString(R.string.su_error_fill_department)
            position.trim().isBlank() -> App.context.getString(R.string.su_error_fill_position)
            email.trim().isBlank() -> App.context.getString(R.string.su_error_fill_email)
            password.isBlank() -> App.context.getString(R.string.su_error_fill_password)
            confirmPassword.isBlank() -> App.context.getString(R.string.su_error_confirm_password)

            fullName.trim().length < 2 -> App.context.getString(R.string.su_error_fullname_min_2)
            department.trim().length < 2 -> App.context.getString(R.string.su_error_department_min_2)
            position.trim().length < 2 -> App.context.getString(R.string.su_error_position_min_2)
            !email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()) ->
                App.context.getString(R.string.su_error_email_invalid)
            password.length < 6 -> App.context.getString(R.string.su_error_password_min_6)
            password != confirmPassword -> App.context.getString(R.string.su_hint_passwords_not_match)
            else -> App.context.getString(R.string.su_error_check_fields)
        }
    }

    private fun updateStateIfData(lambda: (SignUpState.Data) -> SignUpState) {
        _uiState.update { state ->
            (state as? SignUpState.Data)?.let { lambda.invoke(it) } ?: state
        }
    }
}