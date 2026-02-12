package com.example.planify.main.features.settings.domain.services_impl

import com.example.planify.main.common.entities.ThemeId
import com.example.planify.main.features.settings.domain.entities.LocalSettings
import com.example.planify.main.features.settings.domain.repositories.SettingsRepository
import com.example.planify.main.features.settings.domain.services.SettingsService
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class SettingsServiceImpl @Inject constructor(
    val settingsRepository: SettingsRepository
) : SettingsService {

    override val settingsFlow: Flow<LocalSettings>
        get() = settingsRepository.settingsFlow

    override suspend fun setTheme(theme: ThemeId): Result<Unit> {
        return settingsRepository.setTheme(theme)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean): Result<Unit> {
        return settingsRepository.setNotificationsEnabled(enabled)
    }

    override suspend fun setIsFirstStart(isFirstStart: Boolean): Result<Unit> {
        return settingsRepository.setIsFirstStart(isFirstStart)
    }
}
