package com.example.authorization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authorization.domain.CheckAuthUseCase
import com.example.authorization.domain.entites.UserLoginEntity
import com.example.comon.RegisterResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val checkAuthUseCase: CheckAuthUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun authorize(user: UserLoginEntity) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            when (val result = checkAuthUseCase(user)) {
                is RegisterResult.Success -> {
                    _uiState.value = AuthUiState.Success
                }
                is RegisterResult.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
            }
        }
    }

}

