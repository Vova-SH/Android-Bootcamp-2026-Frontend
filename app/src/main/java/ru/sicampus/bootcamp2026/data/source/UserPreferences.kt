package ru.sicampus.bootcamp2026.data.source

import android.content.Context

class UserPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun saveUserEmail(email: String) {
        prefs.edit().putString("user_email", email).apply()
        println("DEBUG: Email saved: $email")
    }

    fun getUserEmail(): String? {
        return prefs.getString("user_email", null)
    }
}