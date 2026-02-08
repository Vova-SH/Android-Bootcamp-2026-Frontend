package com.example.planify.main.navigation.screens.chat_screen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(ChatUIState.Loading)
    val uiState = _uiState.asStateFlow()
}