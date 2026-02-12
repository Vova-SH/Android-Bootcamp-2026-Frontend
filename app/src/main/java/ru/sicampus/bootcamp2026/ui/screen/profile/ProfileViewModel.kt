package ru.sicampus.bootcamp2026.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.profile.ChangeUserUseCase


class ProfileViewModel : ViewModel() {
    private val changeUserUseCase = ChangeUserUseCase(
        userRepository = UserRepository(AuthNetworkDataSource(), UserInfoDataSource())
    )
    private val _uiState : MutableStateFlow<ProfileState> =  MutableStateFlow(ProfileState.Loading)
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.Send -> {
                viewModelScope.launch {
                    val result = changeUserUseCase.invoke(intent.id, intent.email, intent.fullname)
                    result.fold(
                        onSuccess = {
                            println("ProfileViewModel: Change successful")
                        },
                        onFailure = { error ->
                            _uiState.emit(ProfileState.Error(error.message.orEmpty()))
                        }
                    )
                }
            }
        }
    }
}