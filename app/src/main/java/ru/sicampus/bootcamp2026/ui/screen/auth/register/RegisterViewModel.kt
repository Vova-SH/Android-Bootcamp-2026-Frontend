package ru.sicampus.bootcamp2026.ui.screen.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.auth.RegisterEmployeeUseCase
import ru.sicampus.bootcamp2026.domain.auth.ValidateRegistrationDataUseCase

class RegisterViewModel : ViewModel() {
    private val validateUseCase by lazy { ValidateRegistrationDataUseCase() }
    private val registerEmployeeUseCase by lazy { RegisterEmployeeUseCase(
        AuthRepository(AuthNetworkDataSource(), AuthLocalDataSource)
    ) }

    private val _fields = MutableStateFlow(RegisterFields())
    val fields = _fields.asStateFlow()

    private val _uiState = MutableStateFlow<RegisterState>(RegisterState.Data())
    val uiState = _uiState.asStateFlow()



    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.FieldChanged -> processFieldChange(intent.fields)
            RegisterIntent.Submit -> sendRegistration()
        }
    }

    fun onFieldFocusChanged(fieldKey: String, isFocused: Boolean) {
        if (!isFocused) {
            _uiState.update { state ->
                if (state is RegisterState.Data) {
                    val newState = state.copy(visitedFields = state.visitedFields + fieldKey)
                    val validation = validateUseCase(_fields.value)
                    newState.copy(
                        fieldErrors = filterErrors(validation.errors, newState.visitedFields, _fields.value)
                    )
                } else state
            }
        }
    }

    private fun processFieldChange(newFields: RegisterFields) {
        _fields.value = newFields
        updateUiStateWithValidation(newFields)
    }


    private fun sendRegistration() {
        val validation = validateUseCase(_fields.value)
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
            registerEmployeeUseCase(_fields.value).fold(
                onSuccess = { _uiState.value = RegisterState.Success },
                onFailure = { error ->
                    _uiState.value = RegisterState.Data(
                        error = error.message ?: "Ошибка регистрации",
                        isEnabledSend = true,
                        visitedFields = setOf("name", "username", "password", "position", "email", "phoneNumber"),
                        fieldErrors = validateUseCase(_fields.value).errors
                    )
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
                    fieldErrors = filterErrors(validation.errors, state.visitedFields, currentFields),
                    visitedFields = state.visitedFields
                )
            } else state
        }
    }
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