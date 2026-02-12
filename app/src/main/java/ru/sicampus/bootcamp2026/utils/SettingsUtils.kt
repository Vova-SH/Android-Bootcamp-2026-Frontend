package ru.sicampus.bootcamp2026.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import ru.sicampus.bootcamp2026.core.SettingConstants


class SettingsUtils(context: Context) {
    private val settings: SharedPreferences = context.getSharedPreferences(
        SettingConstants.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    // ===== setters =====

    fun setProfileData(userId: Long, email: String, password: String) {
        settings.edit {
            putLong(SettingConstants.USER_ID, userId)
            putString(SettingConstants.EMAIL, email)
            putString(SettingConstants.PASSWORD, password)
        }
    }

    // ===== getters =====

    fun getUserId(): Long {
        return settings.getLong(SettingConstants.USER_ID, -1)
    }

    fun getEmail(): String? {
        return settings.getString(SettingConstants.EMAIL, null)
    }

    fun getPassword(): String? {
        return settings.getString(SettingConstants.PASSWORD, null)
    }

    // ===== clears =====

    fun clear() {
        settings.edit { clear() }
    }

    fun clearProfileData() {
        settings.edit {
            remove(SettingConstants.USER_ID)
            remove(SettingConstants.EMAIL)
            remove(SettingConstants.PASSWORD)
        }
    }

    // ===== utils =====

    fun checkProfileExists(): Boolean {
        val userId: Long = settings.getLong(SettingConstants.USER_ID, -1)
        val email: String? = settings.getString(SettingConstants.EMAIL, null)
        val password: String? = settings.getString(SettingConstants.PASSWORD, null)

        Log.d(
            "test",
            "(SettingsUtils.checkCodeExists) данные, сохранённые в настройках: $userId, $email, $password"
        )

        return userId != -1L && email != null && password != null
    }
}