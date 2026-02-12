package com.example.planify.main.navigation.screens.chat_screen

sealed interface ChatUIState {
    data object Loading: ChatUIState
}