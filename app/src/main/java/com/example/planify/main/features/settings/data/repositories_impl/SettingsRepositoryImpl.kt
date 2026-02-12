package com.example.planify.main.features.settings.data.repositories_impl

import android.content.Context
import com.example.planify.main.common.entities.ThemeId
import com.example.planify.main.features.settings.domain.repositories.SettingsRepository
import com.example.planify.main.features.settings.domain.entities.LocalSettings
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.planify.main.features.settings.data.preferences.SettingsPreferences
import com.example.planify.main.features.settings.data.preferences.settingsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor (
    @ApplicationContext private val context: Context
) : SettingsRepository {
    private val dataStore = context.settingsDataStore

    private fun fromPrimitives(
        theme: String?,
        notifications: Boolean?,
        isFirstStart: Boolean?
    ): LocalSettings {
        val themeId = ThemeId.entries.firstOrNull { it.name == theme }

        return LocalSettings(
            theme = themeId,
            notifications = notifications,
            isFirstStart = isFirstStart
        )
    }

    override val settingsFlow: Flow<LocalSettings> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            fromPrimitives(
                theme = preferences[SettingsPreferences.THEME],
                notifications = preferences[SettingsPreferences.NOTIFICATIONS],
                isFirstStart = preferences[SettingsPreferences.IS_FIRST_START]
            )
        }

    override suspend fun setTheme(theme: ThemeId): Result<Unit>  = runCatching{
        dataStore.edit { it[SettingsPreferences.THEME] = theme.name }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean): Result<Unit> = runCatching {
        dataStore.edit { it[SettingsPreferences.NOTIFICATIONS] = enabled }
    }

    override suspend fun setIsFirstStart(isFirstStart: Boolean): Result<Unit> = runCatching {
        dataStore.edit { it[SettingsPreferences.IS_FIRST_START] = isFirstStart }
    }
}
