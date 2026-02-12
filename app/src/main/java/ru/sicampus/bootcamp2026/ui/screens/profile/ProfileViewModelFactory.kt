package ru.sicampus.bootcamp2026.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.UserDataSource
import ru.sicampus.bootcamp2026.domain.usecase.user.GetUserByIdUseCase
import ru.sicampus.bootcamp2026.domain.usecase.user.SearchUserUseCase
import ru.sicampus.bootcamp2026.domain.usecase.user.UserUpdateUseCase
import ru.sicampus.bootcamp2026.utils.SettingsUtils

object ProfileViewModelFactory {
    fun create(context: Context): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val authLocalDataSource = AuthLocalDataSource
            val userDataSource = UserDataSource()
            val settingsUtils = SettingsUtils(context)
            val userRepository = UserRepository(userDataSource, authLocalDataSource, settingsUtils)
            val getUserByIdUseCase = GetUserByIdUseCase(userRepository)
            val updateUseCase = UserUpdateUseCase(userRepository)
            val searchUserUseCase = SearchUserUseCase(userRepository)

            ProfileViewModel(getUserByIdUseCase, updateUseCase, searchUserUseCase)
        }
    }
}