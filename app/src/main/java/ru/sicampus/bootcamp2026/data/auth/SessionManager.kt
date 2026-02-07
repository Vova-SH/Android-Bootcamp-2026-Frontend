package ru.sicampus.bootcamp2026.data.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SessionManager {
    private val _logoutSignal = MutableSharedFlow<Unit>(replay = 0)
    val logoutSignal = _logoutSignal.asSharedFlow()

    suspend fun triggerLogout() {
        _logoutSignal.emit(Unit)
    }
}