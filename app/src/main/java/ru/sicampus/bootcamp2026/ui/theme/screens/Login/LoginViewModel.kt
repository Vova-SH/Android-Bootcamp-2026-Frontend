package ru.sicampus.bootcamp2026.ui.theme.screens.Login

import android.widget.Toast
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.domain.auth.CheckAndSaveAuthUseCase
import ru.sicampus.bootcamp2026.domain.auth.CheckAuthFormatUseCase
import ru.sicampus.bootcamp2026.domain.reg.RegistrationUseCase

class LoginViewModel(
    private val appViewModel: AppViewModel,
    private val userPreferences: UserPreferences
): ViewModel() {

    private val checkAndSaveAuthUseCase by lazy { CheckAndSaveAuthUseCase(
        AuthRepository(
            authNetworkDataSource = AuthNetworkDataSource(),
            authLocalDataSource = AuthLocalDataSource
        ),
        userPreferences = userPreferences
    ) }



    private val registrationUseCase by lazy { RegistrationUseCase(
        AuthRepository(
            authNetworkDataSource = AuthNetworkDataSource(),
            authLocalDataSource = AuthLocalDataSource
        ),
        userPreferences = userPreferences

    ) }



    private val CheckAuthFormatUseCase by lazy { CheckAuthFormatUseCase() }

    private val _uiState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Content)

    val uiState = _uiState.asStateFlow()

    init{
        getData()
    }

    fun getData() {
        viewModelScope.launch{
            _uiState.emit(LoginState.Content)
            delay(2000L)

        }
    }

    fun onRegistrationClick(){
        viewModelScope.launch {
            _uiState.emit(LoginState.Content)
        }
    }


    fun onLoginClick(){
        appViewModel.NavigateTo(ViewModelState.Invitations)
    }

    fun LoginClick() {
        viewModelScope.launch {
            _uiState.emit(LoginState.Reg)
        }
    }


    fun onIntent(intent: AuthIntent){
        when(intent){
            is AuthIntent.Send ->{
                viewModelScope.launch {
                    userPreferences.saveUserEmail(intent.email)
                    val authCompleted = checkAndSaveAuthUseCase.invoke(
                        intent.email,
                        intent.password
                    )
                    if (authCompleted.isSuccess) {
                        onLoginClick()
                    }else{
                        _uiState.emit(LoginState.Error("Error"))
                        }
                }
            }
            is AuthIntent.Reg -> {
                viewModelScope.launch {
                    userPreferences.saveUserEmail(intent.email)
                    val regCompeted = registrationUseCase.invoke(
                        intent.fullName,
                        listOf(intent.jobTitle),
                        intent.email,
                        intent.password,
                        intent.passwordConfirm,
                        listOf(intent.department)
                    )

                    if(regCompeted.isSuccess){
                        val loginResult = checkAndSaveAuthUseCase.invoke(
                            intent.email,
                            intent.password
                        )
                        if(loginResult.isSuccess){
                            delay(1500)
                            onLoginClick()
                        }
                        else{
                            _uiState.emit(LoginState.Error("Error"))
                        }
                    }
                    else{
                        _uiState.emit(LoginState.Error("Error"))
                    }
                }
            }
        }
    }

}