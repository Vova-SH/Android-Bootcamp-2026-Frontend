package com.teto.planner.presentation.features.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSharedState @Inject constructor() {
    val login = MutableStateFlow("")
    val password = MutableStateFlow("")
    val name = MutableStateFlow("")
    val telegramNick = MutableStateFlow("")

    fun clear() {
        login.update { "" }
        password.update { "" }
        name.update { "" }
        telegramNick.update { "" }
    }
}