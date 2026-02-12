package com.teto.planner.presentation.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.repository.AuthRepository
import com.teto.planner.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            _uiState.update { ProfileScreenUiState.Loading }

            userRepository.getMe()
                .onSuccess { user ->
                    _uiState.update {
                        ProfileScreenUiState.Success(
                            user = user,
                            name = user.name,
                            telegram = user.telegram ?: "",
                            bio = user.bio ?: ""
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { ProfileScreenUiState.Error(e.message ?: "Unknown error") }
                }
        }
    }

    fun updateAvatar(imageBytes: ByteArray) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                if (currentState is ProfileScreenUiState.Success) {
                    currentState.copy(isAvatarUploading = true)
                } else currentState
            }

            userRepository.uploadAvatar(imageBytes)
                .onSuccess { updatedUser ->
                    _uiState.update { currentState ->
                        if (currentState is ProfileScreenUiState.Success) {
                            currentState.copy(
                                user = updatedUser,
                                isAvatarUploading = false
                            )
                        } else currentState
                    }
                }
                .onFailure {
                    _uiState.update { currentState ->
                        if (currentState is ProfileScreenUiState.Success) {
                            currentState.copy(isAvatarUploading = false)
                        } else currentState
                    }
                }
        }
    }

    fun startEditing() {
        _uiState.update { currentState ->
            if (currentState is ProfileScreenUiState.Success) {
                currentState.copy(isEditing = true)
            } else currentState
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { currentState ->
            if (currentState is ProfileScreenUiState.Success) {
                currentState.copy(name = value)
            } else currentState
        }
    }

    fun onTelegramChange(value: String) {
        _uiState.update { currentState ->
            if (currentState is ProfileScreenUiState.Success) {
                currentState.copy(telegram = value)
            } else currentState
        }
    }

    fun onBioChange(value: String) {
        _uiState.update { currentState ->
            if (currentState is ProfileScreenUiState.Success) {
                currentState.copy(bio = value)
            } else currentState
        }
    }

    fun saveProfile() {
        val currentStateSnapshot = _uiState.value

        if (currentStateSnapshot is ProfileScreenUiState.Success && currentStateSnapshot.canSave) {
            viewModelScope.launch {
                _uiState.update { (it as? ProfileScreenUiState.Success)?.copy(isSubmitting = true) ?: it }

                userRepository.updateMe(
                    name = currentStateSnapshot.name,
                    telegram = currentStateSnapshot.telegram.ifBlank { null },
                    bio = currentStateSnapshot.bio.ifBlank { null }
                ).onSuccess { updatedUser ->
                    _uiState.update {
                        ProfileScreenUiState.Success(
                            user = updatedUser,
                            name = updatedUser.name,
                            telegram = updatedUser.telegram ?: "",
                            bio = updatedUser.bio ?: "",
                            isEditing = false,
                            isSaveSuccessful = true
                        )
                    }
                }.onFailure { e ->
                    _uiState.update { (it as? ProfileScreenUiState.Success)?.copy(isSubmitting = false) ?: it }
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}