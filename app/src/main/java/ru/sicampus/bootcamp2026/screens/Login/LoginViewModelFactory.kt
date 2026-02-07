package ru.sicampus.bootcamp2026.screens.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.ui.auth.LoginViewModel

class LoginViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val authRepository = AuthRepository(
                authNetworkDataSource = AuthNetworkDataSource(),
                authLocalDataSource = AuthLocalDataSource.getInstance()
            )
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}