package com.example.planify.main.features.settings.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore by preferencesDataStore(name = "settings")

object SettingsPreferences {
    val NOTIFICATIONS = booleanPreferencesKey("notifications")
    val THEME = stringPreferencesKey("theme_id")
    val IS_FIRST_START = booleanPreferencesKey("is_first_start")
}
