package ru.sicampus.bootcamp2026.screens.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource

class RegisterViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val authRepository = AuthRepository(
                authNetworkDataSource = AuthNetworkDataSource(),
                authLocalDataSource = AuthLocalDataSource.getInstance()
            )
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}