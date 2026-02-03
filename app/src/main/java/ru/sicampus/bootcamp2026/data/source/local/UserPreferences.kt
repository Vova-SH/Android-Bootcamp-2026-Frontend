package ru.sicampus.bootcamp2026.data.source.local

import android.content.Context


class UserPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(
        "user_data",
        Context.MODE_PRIVATE
    )

    fun saveEmail(email: String) = prefs.edit().putString("user_email", email).apply()
    fun getEmail(): String? = prefs.getString("user_email", null)
}