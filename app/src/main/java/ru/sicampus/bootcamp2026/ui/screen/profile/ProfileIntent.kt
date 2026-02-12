package ru.sicampus.bootcamp2026.ui.screen.profile

interface ProfileIntent {
    data class Send(val id: Int, val email: String, val fullname: String): ProfileIntent
}