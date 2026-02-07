package ru.sicampus.bootcamp2026

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sicampus.bootcamp2026.data.repository.AuthRepositoryImpl
import ru.sicampus.bootcamp2026.data.repository.MeetingRepositoryImpl
import ru.sicampus.bootcamp2026.data.repository.UserRepositoryImpl
import ru.sicampus.bootcamp2026.domain.usecase.*
import ru.sicampus.bootcamp2026.ui.screen.auth.AuthViewModel
import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingsViewModel
import ru.sicampus.bootcamp2026.ui.screen.profile.ProfileViewModel

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory : ViewModelProvider.Factory {
    private val authRepository by lazy { AuthRepositoryImpl() }
    private val meetingRepository by lazy { MeetingRepositoryImpl() }
    private val userRepository by lazy { UserRepositoryImpl() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(
                    LoginUseCase(authRepository),
                    RegisterUseCase(authRepository)
                ) as T
            }
            modelClass.isAssignableFrom(MeetingsViewModel::class.java) -> {
                MeetingsViewModel(
                    GetMeetingsUseCase(meetingRepository),
                    CreateMeetingUseCase(meetingRepository),
                    DeleteMeetingUseCase(meetingRepository)
                ) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    GetUserProfileUseCase(userRepository),
                    UpdateUserProfileUseCase(userRepository)
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}