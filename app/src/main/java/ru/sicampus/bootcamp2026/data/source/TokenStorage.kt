package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import android.content.SharedPreferences

object TokenStorage {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_USER_ID = "user_id"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var accessToken: String?
        get() = if (::prefs.isInitialized) prefs.getString(KEY_ACCESS_TOKEN, null) else null
        set(value) {
            if (::prefs.isInitialized) {
                if (value != null) {
                    prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()
                } else {
                    prefs.edit().remove(KEY_ACCESS_TOKEN).apply()
                }
            }
        }

    var userId: Long?
        get() = if (::prefs.isInitialized && prefs.contains(KEY_USER_ID)) prefs.getLong(KEY_USER_ID, -1L) else null
        set(value) {
            if (::prefs.isInitialized) {
                if (value != null) {
                    prefs.edit().putLong(KEY_USER_ID, value).apply()
                } else {
                    prefs.edit().remove(KEY_USER_ID).apply()
                }
            }
        }

    fun clear() {
        if (::prefs.isInitialized) {
            prefs.edit().clear().apply()
        }
    }
}