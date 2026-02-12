package ru.sicampus.bootcamp2026.ui.screens.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.sicampus.bootcamp2026.data.repository.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.usecase.auth.RegisterUseCase
import ru.sicampus.bootcamp2026.utils.SettingsUtils

object SignUpViewModelFactory {
    fun create(context: Context): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val authNetworkDataSource = AuthNetworkDataSource()
            val authLocalDataSource = AuthLocalDataSource
            val settingsUtils = SettingsUtils(context)
            val authRepository = AuthRepository(authNetworkDataSource, authLocalDataSource, settingsUtils)
            val loginUseCase = RegisterUseCase(authRepository)

            SignUpViewModel(loginUseCase)
        }
    }
}