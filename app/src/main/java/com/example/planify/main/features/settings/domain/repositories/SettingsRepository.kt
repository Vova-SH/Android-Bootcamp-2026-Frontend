package com.example.planify.main.features.settings.domain.repositories

import com.example.planify.main.common.entities.ThemeId
import com.example.planify.main.features.settings.domain.entities.LocalSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<LocalSettings>
    suspend fun setTheme(theme: ThemeId): Result<Unit>
    suspend fun setNotificationsEnabled(enabled: Boolean): Result<Unit>
    suspend fun setIsFirstStart(isFirstStart: Boolean): Result<Unit>
}
