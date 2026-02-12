package com.example.frontend.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesManager(private val ctx: Context) {
    
    companion object {
        private val Context.ds: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
        
        private val uid = longPreferencesKey("user_id")
        private val uname = stringPreferencesKey("user_name")
        private val ulogin = stringPreferencesKey("user_login")
        private val fcm = stringPreferencesKey("fcm_token")
    }
    
    val userIdFlow: Flow<Long?> = ctx.ds.data.map { it[uid] }
    
    val userNameFlow: Flow<String?> = ctx.ds.data.map { it[uname] }
    
    val fcmTokenFlow: Flow<String?> = ctx.ds.data.map { it[fcm] }
    
    suspend fun saveUserData(id: Long, name: String, login: String) {
        ctx.ds.edit { p ->
            p[uid] = id
            p[uname] = name
            p[ulogin] = login
        }
    }
    
    suspend fun saveFcmToken(token: String) {
        ctx.ds.edit { p ->
            p[fcm] = token
        }
    }
    
    suspend fun clearUserData() {
        ctx.ds.edit { it.clear() }
    }
}

object DateFormatter {
    
    fun formatDate(d: String): String {
        val p = d.split("-")
        if (p.size == 3) {
            return "${p[2]}.${p[1]}.${p[0]}"
        }
        return d
    }
    
    fun formatTime(t: String): String {
        if (t.length >= 5) {
            return t.substring(0, 5)
        }
        return t
    }
    
    fun formatDateTime(d: String, t: String): String {
        return "${formatDate(d)} в ${formatTime(t)}"
    }
}

object AppConstants {
    const val minPass = 6
    const val maxName = 50
    const val notifCode = 1001
    const val dateFmt = "yyyy-MM-dd"
    const val timeFmt = "HH:mm"
    const val displayDateFmt = "dd.MM.yyyy"
}

object Validator {
    
    fun isValidName(n: String): Boolean {
        if (n.isBlank()) return false
        if (n.length > AppConstants.maxName) return false
        return true
    }
    
    fun isValidLogin(l: String): Boolean {
        if (l.isBlank()) return false
        if (l.length < 3) return false
        return l.matches(Regex("^[a-zA-Z0-9_]+$"))
    }
    
    fun isValidPassword(p: String): Boolean {
        return p.length >= AppConstants.minPass
    }
    
    fun getPasswordError(p: String): String? {
        if (p.isEmpty()) return "Введите пароль"
        if (p.length < AppConstants.minPass) {
            return "Пароль должен быть не менее ${AppConstants.minPass} символов"
        }
        return null
    }
}
