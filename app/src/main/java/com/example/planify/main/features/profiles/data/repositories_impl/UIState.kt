package com.example.planify.main.features.profiles.data.repositories_impl


sealed interface EditProfileUIState {
    data object Loading: EditProfileUIState
    data object Idle : EditProfileUIState
    data object Saving : EditProfileUIState
    data object Saved : EditProfileUIState
    data class Error(val message: String): EditProfileUIState
}