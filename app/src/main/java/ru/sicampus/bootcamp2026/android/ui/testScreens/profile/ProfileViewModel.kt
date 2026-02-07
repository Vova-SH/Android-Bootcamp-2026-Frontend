package ru.sicampus.bootcamp2026.android.ui.testScreens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.android.data.ProfileRepository
import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.ProfileNetworkDataSource
import ru.sicampus.bootcamp2026.android.domain.profile.GetProfileUseCase
import ru.sicampus.bootcamp2026.android.domain.profile.LogoutUseCase
import ru.sicampus.bootcamp2026.android.domain.profile.UpdateProfileUseCase
import ru.sicampus.bootcamp2026.android.ui.nav.AuthRoute

class ProfileViewModel : ViewModel() {
    private val profileRepository by lazy {
        ProfileRepository(
            profileNetworkDataSource = ProfileNetworkDataSource(),
            authLocalDataSource = AuthLocalDataSource
        )
    }

    private val getProfileUseCase by lazy { GetProfileUseCase(profileRepository) }
    private val updateProfileUseCase by lazy { UpdateProfileUseCase(profileRepository) }
    private val logoutUseCase by lazy { LogoutUseCase(profileRepository) }

    private val _uiState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ProfileAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    init {
        loadProfile()
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfile -> loadProfile()
            
            is ProfileIntent.ToggleEditMode -> {
                updateStateIfData { oldState ->
                    oldState.copy(isEditMode = !oldState.isEditMode, error = null)
                }
            }

            is ProfileIntent.UpdateProfile -> {
                _uiState.value = ProfileState.Loading
                viewModelScope.launch {
                    updateProfileUseCase.invoke(
                        fullName = intent.fullName,
                        department = intent.department,
                        position = intent.position
                    ).fold(
                        onSuccess = { personResponse ->
                            _uiState.value = ProfileState.Data(
                                email = personResponse.email,
                                fullName = personResponse.fullName,
                                department = personResponse.department,
                                position = personResponse.position,
                                isEditMode = false,
                                error = null
                            )
                        },
                        onFailure = { error ->
                            updateStateIfData { oldState ->
                                oldState.copy(
                                    error = error.message ?: "Ошибка обновления профиля",
                                    isEditMode = true
                                )
                            }
                        }
                    )
                }
            }

            is ProfileIntent.Logout -> {
                viewModelScope.launch {
                    logoutUseCase.invoke()
                    _actionFlow.emit(ProfileAction.NavigateTo(AuthRoute))
                }
            }
        }
    }

    private fun loadProfile() {
        _uiState.value = ProfileState.Loading
        viewModelScope.launch {
            getProfileUseCase.invoke().fold(
                onSuccess = { personResponse ->
                    _uiState.value = ProfileState.Data(
                        email = personResponse.email,
                        fullName = personResponse.fullName,
                        department = personResponse.department,
                        position = personResponse.position,
                        isEditMode = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = ProfileState.Data(
                        email = "",
                        fullName = "",
                        department = "",
                        position = "",
                        isEditMode = false,
                        error = error.message ?: "Ошибка загрузки профиля"
                    )
                }
            )
        }
    }

    private fun updateStateIfData(lambda: (ProfileState.Data) -> ProfileState) {
        _uiState.update { state ->
            (state as? ProfileState.Data)?.let { lambda.invoke(it) } ?: state
        }
    }
}