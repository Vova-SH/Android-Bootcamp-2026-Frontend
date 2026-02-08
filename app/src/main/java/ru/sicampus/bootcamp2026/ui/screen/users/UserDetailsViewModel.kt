package ru.sicampus.bootcamp2026.ui.screen.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.domain.usecase.GetUserByIdUseCase

class UserDetailsViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserDetailsUiState>(UserDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadUser(userId: Long) {
        viewModelScope.launch {
            _uiState.value = UserDetailsUiState.Loading
            getUserByIdUseCase(userId)
                .onSuccess { _uiState.value = UserDetailsUiState.Content(it) }
                .onFailure { _uiState.value = UserDetailsUiState.Error(it.message ?: "Unknown error") }
        }
    }
}