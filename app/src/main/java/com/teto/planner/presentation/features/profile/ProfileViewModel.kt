package com.teto.planner.presentation.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.repository.AuthRepository
import com.teto.planner.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileScreenUiState>(ProfileScreenUiState.Loading)
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileScreenUiState.Loading
            userRepository.getMe().onSuccess { user ->
                _uiState.value = ProfileScreenUiState.Success(
                    user = user,
                    name = user.name,
                    telegram = user.telegram ?: "",
                    bio = user.bio ?: ""
                )
            }.onFailure { e ->
                _uiState.value = ProfileScreenUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun startEditing() {
        val currentState = _uiState.value
        if (currentState is ProfileScreenUiState.Success) {
            _uiState.value = currentState.copy(isEditing = true)
        }
    }

    fun onNameChange(value: String) {
        val currentState = _uiState.value
        if (currentState is ProfileScreenUiState.Success) {
            _uiState.value = currentState.copy(name = value)
        }
    }

    fun onTelegramChange(value: String) {
        val currentState = _uiState.value
        if (currentState is ProfileScreenUiState.Success) {
            _uiState.value = currentState.copy(telegram = value)
        }
    }

    fun onBioChange(value: String) {
        val currentState = _uiState.value
        if (currentState is ProfileScreenUiState.Success) {
            _uiState.value = currentState.copy(bio = value)
        }
    }

    fun saveProfile() {
        val currentState = _uiState.value
        if (currentState is ProfileScreenUiState.Success && currentState.canSave) {
            viewModelScope.launch {
                _uiState.value = currentState.copy(isSubmitting = true)
                userRepository.updateMe(
                    name = currentState.name,
                    telegram = currentState.telegram.ifBlank { null },
                    bio = currentState.bio.ifBlank { null }
                ).onSuccess { updatedUser ->
                    _uiState.value = ProfileScreenUiState.Success(
                        user = updatedUser,
                        name = updatedUser.name,
                        telegram = updatedUser.telegram ?: "",
                        bio = updatedUser.bio ?: "",
                        isEditing = false,
                        isSaveSuccessful = true
                    )
                }.onFailure { e ->
                    _uiState.value = currentState.copy(isSubmitting = false)
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}