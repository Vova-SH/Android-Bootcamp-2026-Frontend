package com.example.planify.main.navigation.screens.main_screen.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.features.auth.domain.entities.UserPrivate
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.features.profiles.domain.entities.Profile
import com.example.planify.main.features.profiles.domain.services.ProfilesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val profilesService: ProfilesService,
    val authService: AuthService
) : ViewModel() {
    val myProfile = profilesService.myProfile
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow<UIState>(UIState.Loading)

    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        getOrFetchUserInfo()
        viewModelScope.launch {
            myProfile.collect { profile ->
                if (_uiState.value is UIState.ContentData && profile != null) {
                    _uiState.update { (_uiState.value as UIState.ContentData).copy(profile = profile) }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
        }
    }

    fun getOrFetchUserInfo() {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading)

            var userInfo: UserPrivate?
            var profileInfo: Profile?

            try {
                val userFetching = async { authService.fetchMe() }
                val profileFetching = async { profilesService.fetchMyProfile() }

                val userRes = userFetching.await()
                val profileRes = profileFetching.await()

                userInfo = userRes.getOrThrow()
                profileInfo = profileRes.getOrThrow()
            } catch (error: Exception) {
                _uiState.emit(
                    UIState.Error(error.message.orEmpty())
                )
                return@launch
            }

            _uiState.emit(
                UIState.ContentData(
                    user = userInfo,
                    profile = profileInfo
                )
            )
        }
    }
}