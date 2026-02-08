package com.example.planify.main.features.settings.domain.entities

import com.example.planify.main.common.entities.ThemeId

object SettingsDefaults {
    val THEME: ThemeId = ThemeId.SYSTEM
    const val NOTIFICATIONS: Boolean = true
    const val IS_FIRST_START: Boolean = true
}
