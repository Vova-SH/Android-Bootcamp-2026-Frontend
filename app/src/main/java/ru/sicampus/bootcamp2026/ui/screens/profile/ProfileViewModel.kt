package ru.sicampus.bootcamp2026.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetProfileUseCase
import ru.sicampus.bootcamp2026.domain.UpdateProfileUseCase

class ProfileViewModel: ViewModel() {

    private val getProfileUseCase = GetProfileUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )

    private val updateProfileUseCase = UpdateProfileUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )
    private val _uiState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.emit(ProfileState.Loading)
            getProfileUseCase.invoke()?.fold(
                onSuccess = { user ->
                    _uiState.emit(ProfileState.Content(user))
                },
                onFailure = { error ->
                    _uiState.emit(ProfileState.Error(error.message.orEmpty()))
                }
            )
            _uiState.emit(ProfileState.Error("Ошибка получения данных"))
        }
    }

    fun updateData(
        name: String?,
        phone: String?,
        email: String?,
        info: String?,
        photoUrl: String?
    ) {
        viewModelScope.launch {
            _uiState.emit(ProfileState.Loading)
            updateProfileUseCase.invoke(name, phone, email, info, photoUrl).fold(
                onSuccess = { getData() },
                onFailure = { error ->
                    _uiState.emit(ProfileState.Error(error.message.orEmpty()))
                }
            )
            _uiState.emit(ProfileState.Error("Ошибка обновления данных"))

        }
    }
}
