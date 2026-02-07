package ru.sicampus.bootcamp2026.data.model

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SharedEvents {
    private val _meetingsUpdated = MutableSharedFlow<Unit>()
    val meetingsUpdated = _meetingsUpdated.asSharedFlow()

    suspend fun notifyMeetingsUpdated() {
        _meetingsUpdated.emit(Unit)
    }
}