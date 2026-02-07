package ru.sicampus.bootcamp2026.android.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.sicampus.bootcamp2026.App
import kotlin.io.encoding.Base64

object AuthLocalDataSource {

    private var isInit = false
    private var _cacheToken: String? = null
    private var _cacheUserId: Long? = null

    suspend fun getToken(): String? {
        if (!isInit) {
            _cacheToken = App.context.dataStore.data.map { preferences ->
                preferences[TOKEN]
            }.firstOrNull()
            isInit = true
        }
        return _cacheToken
    }

    suspend fun setToken(login: String, password: String) {
        val decodePhrase = "$login:$password"
        val token = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
        _cacheToken = token
        App.context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().also { preferences ->
                preferences[TOKEN] = token
            }
        }
    }

    suspend fun getUserId(): Long? {
        if (_cacheUserId == null) {
            _cacheUserId = App.context.dataStore.data.map { preferences ->
                preferences[USER_ID]
            }.firstOrNull()
        }
        return _cacheUserId
    }

    suspend fun setUserId(userId: Long) {
        _cacheUserId = userId
        App.context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().also { preferences ->
                preferences[USER_ID] = userId
            }
        }
    }

    fun clearToken() {
        _cacheToken = null
    }

    fun clearUserId() {
        _cacheUserId = null
    }

    suspend fun clearAll() {
        clearToken()
        clearUserId()
        App.context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                remove(TOKEN)
                remove(USER_ID)
            }
        }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val TOKEN = stringPreferencesKey("token")
    private val USER_ID = longPreferencesKey("user_id")
}