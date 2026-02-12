package com.example.planify.main.navigation.screens.main_screen.views.profile.ui.edit_profile

import android.webkit.WebStorage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.features.auth.domain.entities.UserPrivate
import com.example.planify.main.features.auth.domain.services.AuthService
import com.example.planify.main.features.profiles.data.repositories_impl.EditProfileUIState as UIState
import com.example.planify.main.features.profiles.domain.entities.Profile
import com.example.planify.main.features.profiles.domain.schemas.PatchMyProfileSchema
import com.example.planify.main.features.profiles.domain.services.ProfilesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profilesService: ProfilesService,
    private val authService: AuthService
): ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _originalProfile = MutableStateFlow<Profile?>(null)
    val originalProfile = _originalProfile.asStateFlow()

    private val _user = MutableStateFlow<UserPrivate?>(null)
    val user = _user.asStateFlow()

    private val _editProfileDraftState = MutableStateFlow(EditProfileDraftState.empty())

    val editProfileDraftState = _editProfileDraftState.asStateFlow()

    private fun fetchMyUserAndProfile() = viewModelScope.launch {
        _uiState.value = UIState.Loading

        val me = async { authService.fetchMe() }
        val myProfile = async { profilesService.fetchMyProfile() }

        val meResponse = me.await()
        val myProfileResponse = myProfile.await()

        meResponse
            .onSuccess { me ->
                _user.value = me
            }
            .onFailure { error ->
                _uiState.value = UIState.Error(error.message ?: "Fetching user error")
            }

        myProfileResponse
            .onSuccess { profile ->
                _originalProfile.value = profile
                _editProfileDraftState.value = EditProfileDraftState.fromProfile(profile)
            }
            .onFailure { error ->
                _uiState.value = UIState.Error(error.message ?: "Loading profile error")
            }

        if (meResponse.isSuccess && myProfileResponse.isSuccess) {
            _uiState.value = UIState.Idle
        }
    }

    init {
        fetchMyUserAndProfile()
    }

    fun setFirstName(v: String) = _editProfileDraftState.update { it.copy(firstName = v) }
    fun setLastName(v: String) = _editProfileDraftState.update { it.copy(lastName = v) }
    fun setDepartment(v: String) = _editProfileDraftState.update { it.copy(department = v) }
    fun setPosition(v: String) = _editProfileDraftState.update { it.copy(position = v) }
    fun setImageUrl(v: String) = _editProfileDraftState.update { it.copy(profileImageUrl = v) }

    fun save() = viewModelScope.launch {
        val draft = _editProfileDraftState.value
        _uiState.value = UIState.Saving

        val shema = PatchMyProfileSchema(
            firstName = draft.firstName,
            lastName = draft.lastName,
            position = draft.position,
            department = draft.department,
            profileImageUrl = draft.profileImageUrl
        )

        profilesService.patchMyProfile(shema)
            .onSuccess {
                profilesService.fetchMyProfile()
                    .onSuccess { new ->
                        _originalProfile.value = new
                        _editProfileDraftState.value = EditProfileDraftState.fromProfile(new)
                        _uiState.value = UIState.Saved
                    }
                    .onFailure { error ->
                        _uiState.value = UIState.Error(error.message ?: "Updating error")
                    }
            }
            .onFailure { error ->
                _uiState.value = UIState.Error(error.message ?: "Saving changes error")
            }
    }

}