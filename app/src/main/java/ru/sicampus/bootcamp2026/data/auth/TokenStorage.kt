package ru.sicampus.bootcamp2026.data.auth

import android.content.Context

class TokenStorage(private val context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getToken(): String? = prefs.getString("token", null)

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun clearToken() {
        prefs.edit().remove("token").apply()
    }

    fun saveUserId(id: Long) = prefs.edit().putLong("user_id", id).apply()
    fun getUserId(): Long = prefs.getLong("user_id", -1)
    fun clear() = prefs.edit().clear().apply()
}
