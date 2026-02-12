package ru.sicampus.bootcamp2026.ui.screen.auth.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.repository.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.auth.RegisterEmployeeUseCase
import ru.sicampus.bootcamp2026.domain.auth.ValidateRegistrationDataUseCase

class RegisterViewModel : ViewModel() {
    private val validateUseCase by lazy { ValidateRegistrationDataUseCase() }
    private val registerEmployeeUseCase by lazy {
        RegisterEmployeeUseCase(AuthRepository(AuthNetworkDataSource(), AuthLocalDataSource))
    }

    private val _uiState = MutableStateFlow<RegisterState>(RegisterState.Data())
    val uiState = _uiState.asStateFlow()

    // Поля ввода (TextFieldState)
    val nameState = TextFieldState()
    val usernameState = TextFieldState()
    val passwordState = TextFieldState()
    val positionState = TextFieldState()
    val emailState = TextFieldState()
    val phoneState = TextFieldState()

    init {
        observeFieldsForValidation()
    }

    private fun observeFieldsForValidation() {
        combine(
            snapshotFlow { nameState.text },
            snapshotFlow { usernameState.text },
            snapshotFlow { passwordState.text },
            snapshotFlow { positionState.text },
            snapshotFlow { emailState.text },
            snapshotFlow { phoneState.text }
        ) { values: Array<CharSequence> ->
            RegisterFields(
                name = values[0].toString(),
                username = values[1].toString(),
                password = values[2].toString(),
                position = values[3].toString(),
                email = values[4].toString(),
                phoneNumber = values[5].toString()
            )
        }.onEach { currentFields ->
            updateUiStateWithValidation(currentFields)
        }.launchIn(viewModelScope)
    }

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Submit -> sendRegistration()
        }
    }

    fun onFieldFocusChanged(fieldKey: String, isFocused: Boolean) {
        if (!isFocused) {
            _uiState.update { state ->
                if (state is RegisterState.Data) {
                    val visited = state.visitedFields + fieldKey
                    val currentFields = getCurrentFields()
                    val validation = validateUseCase(currentFields)
                    state.copy(
                        visitedFields = visited,
                        fieldErrors = filterErrors(validation.errors, visited, currentFields)
                    )
                } else state
            }
        }
    }

    private fun sendRegistration() {
        val currentFields = getCurrentFields()
        val validation = validateUseCase(currentFields)

        if (!validation.isValid) {
            _uiState.update { state ->
                if (state is RegisterState.Data) {
                    state.copy(
                        visitedFields = setOf("name", "username", "password", "position", "email", "phoneNumber"),
                        fieldErrors = validation.errors
                    )
                } else state
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterState.Loading
            registerEmployeeUseCase(currentFields).fold(
                onSuccess = { _uiState.value = RegisterState.Success },
                onFailure = { error ->
                    _uiState.update {
                        RegisterState.Data(
                            error = error.message ?: "Ошибка регистрации",
                            isEnabledSend = true,
                            visitedFields = setOf("name", "username", "password", "position", "email", "phoneNumber"),
                            fieldErrors = validateUseCase(currentFields).errors
                        )
                    }
                }
            )
        }
    }

    private fun updateUiStateWithValidation(currentFields: RegisterFields) {
        val validation = validateUseCase(currentFields)
        _uiState.update { state ->
            if (state is RegisterState.Data) {
                state.copy(
                    isEnabledSend = validation.isValid,
                    fieldErrors = filterErrors(validation.errors, state.visitedFields, currentFields)
                )
            } else state
        }
    }

    private fun getCurrentFields() = RegisterFields(
        name = nameState.text.toString(),
        username = usernameState.text.toString(),
        password = passwordState.text.toString(),
        position = positionState.text.toString(),
        email = emailState.text.toString(),
        phoneNumber = phoneState.text.toString()
    )

    private fun filterErrors(
        allErrors: Map<String, String>,
        visited: Set<String>,
        fields: RegisterFields
    ): Map<String, String> {
        return allErrors.filterKeys { key ->
            visited.contains(key) && getFieldValueByKey(fields, key).isNotEmpty()
        }
    }

    private fun getFieldValueByKey(fields: RegisterFields, key: String): String {
        return when(key) {
            "name" -> fields.name
            "username" -> fields.username
            "password" -> fields.password
            "position" -> fields.position
            "email" -> fields.email
            "phoneNumber" -> fields.phoneNumber
            else -> ""
        }
    }
}