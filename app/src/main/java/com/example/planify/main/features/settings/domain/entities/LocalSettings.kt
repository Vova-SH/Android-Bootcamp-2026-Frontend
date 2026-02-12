package com.example.planify.main.features.settings.domain.entities

import com.example.planify.main.common.entities.ThemeId

data class LocalSettings(
    val theme: ThemeId = ThemeId.SYSTEM,
    val notifications: Boolean = true,
    val isFirstStart: Boolean = true
) {
    constructor(
        theme: ThemeId?,
        notifications: Boolean?,
        isFirstStart: Boolean?
    ) : this(
        theme = theme ?: SettingsDefaults.THEME,
        notifications = notifications ?: SettingsDefaults.NOTIFICATIONS,
        isFirstStart = isFirstStart ?: SettingsDefaults.IS_FIRST_START
    )
}
