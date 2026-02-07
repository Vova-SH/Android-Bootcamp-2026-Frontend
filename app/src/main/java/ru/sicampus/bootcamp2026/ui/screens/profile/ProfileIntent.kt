package ru.sicampus.bootcamp2026.ui.screens.profile

sealed interface ProfileIntent {

    object Search : ProfileIntent
    object Update : ProfileIntent
    object Logout : ProfileIntent

}
