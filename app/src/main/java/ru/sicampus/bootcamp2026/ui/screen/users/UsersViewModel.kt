package ru.sicampus.bootcamp2026.ui.screen.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.network.UserRepository
import ru.sicampus.bootcamp2026.data.network.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase
import ru.sicampus.bootcamp2026.domain.SearchUsersUseCase
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class UsersViewModel: ViewModel() {
    private val getUsersUseCase = GetUsersUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )
    private val searchUsersUseCase = SearchUsersUseCase(
        userRepository = UserRepository(UserInfoDataSource())
    )
    private val _uiState: MutableStateFlow<UsersScreenState> = MutableStateFlow(UsersScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            _uiState.emit(UsersScreenState.Loading)
            getUsersUseCase.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(UsersScreenState.Content(data))
                },
                onFailure = { error ->
                    Log.e("ui state", error.message.orEmpty())
                    _uiState.emit(UsersScreenState.Error(error.message.orEmpty()) { getData() })
                }
            )
        }
    }

    fun searchUser(fio: String) {
        if (fio.isEmpty()) return getData()
        viewModelScope.launch {
            _uiState.emit(UsersScreenState.Loading)
            searchUsersUseCase.invoke(fio).fold(
                onSuccess = { data ->
                    _uiState.emit(UsersScreenState.Content(data))
                },
                onFailure = { error ->
                    Log.e("search user", error.message.orEmpty())
                    _uiState.emit(UsersScreenState.Error(error.message.orEmpty()) { getData() })
                }
            )
        }
    }


    fun error(error: String, buttonText: String = "Попробовать ещё раз", onClickButton : () -> Unit) {
        viewModelScope.launch {
            _uiState.emit(UsersScreenState.Error(error, onClickButton = onClickButton, buttonText = buttonText) )
        }
    }



//    fun searchData(search: String) {
//        viewModelScope.launch {
//
//        }
//    }
}