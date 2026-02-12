package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.source.AuthApiService
import ru.sicampus.bootcamp2026.util.ErrorUtils

class ProfileStateModel : ViewModel() {
    private val authApiService = AuthApiService()
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.update { ProfileState.Loading }
            try {
                val user = authApiService.getCurrentUser()
                _uiState.update {
                    ProfileState.Content(
                        fullName = user.fullName ?: "",
                        email = user.email ?: "",
                        pfpUrl = user.pfpUrl
                    )
                }
            } catch (e: Exception) {
                _uiState.update { ProfileState.Error(ErrorUtils.translateError(e, "Неизвестная ошибка")) }
            }
        }
    }

    fun uploadImage(bytes: ByteArray, fileName: String) {
        viewModelScope.launch {
            _uiState.update { ProfileState.Loading }
            try {
                authApiService.uploadPfp(bytes, fileName)
                getData()
            } catch (e: Exception) {
                _uiState.update { ProfileState.Error(ErrorUtils.translateError(e, "Ошибка при загрузке фото")) }
            }
        }
    }
}
