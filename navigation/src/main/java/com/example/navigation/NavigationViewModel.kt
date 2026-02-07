package com.example.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.token_storage.domain.use_cases.GetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
) : ViewModel() {

    private val _hasToken = MutableStateFlow<Boolean>(false)
    val hasToken: StateFlow<Boolean> = _hasToken

    init {
        viewModelScope.launch {
            val token = getTokenUseCase()

            _hasToken.value = !token.isNullOrEmpty()
        }
    }
}