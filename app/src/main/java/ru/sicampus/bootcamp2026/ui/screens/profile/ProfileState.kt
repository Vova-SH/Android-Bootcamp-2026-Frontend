package ru.sicampus.bootcamp2026.ui.screens.profile

sealed interface ProfileState {

    object Loading: ProfileState
    object Data: ProfileState
    object Error: ProfileState
    object EditData : ProfileState

}