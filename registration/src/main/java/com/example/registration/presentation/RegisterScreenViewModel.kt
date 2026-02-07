package com.example.registration.presentation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comon.RegisterResult
import com.example.registration.domain.RegisterUserUseCase
import com.example.registration.domain.UserRegisterEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    var fullName by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var department by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onFullNameChange(value: String) {
        fullName = value
    }

    fun onPhoneNumberChange(value: String) {
        phoneNumber = value
    }

    fun onDepartmentChange(value: String) {
        department = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    private val _state = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun register() {
        viewModelScope.launch {
            _state.value = RegisterUiState.Loading

            val user = UserRegisterEntity(
                fullName = fullName,
                phoneNumber = phoneNumber,
                department = department,
                password = password
            )

            when (val result = registerUserUseCase(user)) {
                is RegisterResult.Success -> {
                    _state.value = RegisterUiState.Success
                }

                is RegisterResult.Error -> {
                    _state.value = RegisterUiState.Error(result.message)
                }
            }
        }

    }
}