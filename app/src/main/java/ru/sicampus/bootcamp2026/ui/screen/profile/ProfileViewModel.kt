package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.repository.AuthRepository
import ru.sicampus.bootcamp2026.data.repository.ProfileRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.ProfileDataSource
import ru.sicampus.bootcamp2026.domain.auth.LogoutEmployeeUseCase
import ru.sicampus.bootcamp2026.domain.entities.EmployeeEntity
import ru.sicampus.bootcamp2026.domain.entities.ProfileUpdateEntity
import ru.sicampus.bootcamp2026.domain.profile.EditProfileUseCase
import ru.sicampus.bootcamp2026.domain.profile.GetProfileUseCase

class ProfileViewModel : ViewModel() {
    private val getProfileUseCase by lazy {
        GetProfileUseCase(
            profileRepository = ProfileRepository(
                profileDataSource = ProfileDataSource()
            )
        )
    }
    private val editProfileUseCase by lazy {
        EditProfileUseCase(
            profileRepository = ProfileRepository(
                profileDataSource = ProfileDataSource()
            )
        )
    }
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()
    private val _uiState : MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loading)
    val uiState = _uiState.asStateFlow()

    val nameState = TextFieldState()
    val positionState = TextFieldState()
    val emailState = TextFieldState()
    val phoneState = TextFieldState()

    init {
        getEmployeeData()
    }
    fun prepareEditMode(user: EmployeeEntity) {
        nameState.edit { replace(0, length, user.name) }
        positionState.edit { replace(0, length, user.position) }
        emailState.edit { replace(0, length, user.email) }
        phoneState.edit { replace(0, length, user.phoneNumber) }
        onIntent(ProfileIntent.SetEditMode)
    }
    private val logoutEmployeeUseCase by lazy {
        LogoutEmployeeUseCase(
            authRepository = AuthRepository(
                authNetworkDataSource = AuthNetworkDataSource(),
                authLocalDataSource = AuthLocalDataSource
            )
        )
    }
    private fun logout(){
        viewModelScope.launch {
            logoutEmployeeUseCase()
        }
    }
    private fun setEditMode(enabled: Boolean) {
        _isEditMode.value = enabled
    }
    private fun getEmployeeData(){
        viewModelScope.launch {
            if (_uiState.value !is ProfileState.Content) {
                _uiState.emit(ProfileState.Loading)
            }
            getProfileUseCase().fold(
                onSuccess = { user ->
                    _uiState.emit(ProfileState.Content(user))
                },
                onFailure = { e ->
                    _uiState.emit(ProfileState.Error(e.message.orEmpty()))
                }
            )
        }
    }
    private fun updateProfile(updatedEmployee: ProfileUpdateEntity) {
        viewModelScope.launch {
            if (_uiState.value !is ProfileState.Content) {
                _uiState.emit(ProfileState.Loading)
            }
            editProfileUseCase(updatedEmployee).fold(
                onSuccess = {
                    setEditMode(false)
                    getEmployeeData()
                },
                onFailure = { e ->
                    _uiState.emit(ProfileState.Error(e.message.orEmpty()))
                }
            )

        }
    }
    fun onIntent(intent: ProfileIntent){
        when(intent){
            is ProfileIntent.Cancel -> {
                setEditMode(false)
                getEmployeeData()
            }
            is ProfileIntent.Logout -> logout()
            is ProfileIntent.Save -> updateProfile(intent.updatedEmployee)
            is ProfileIntent.SetEditMode -> setEditMode(true)
            is ProfileIntent.Load -> getEmployeeData()
        }
    }

}