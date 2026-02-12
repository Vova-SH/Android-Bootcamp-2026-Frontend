package com.example.planify.main.navigation

sealed interface DialogType {
    object AuthError : DialogType
    data class Generic(val title: String, val message: String) : DialogType
}
