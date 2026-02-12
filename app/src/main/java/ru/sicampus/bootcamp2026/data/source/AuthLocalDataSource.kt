package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.sicampus.bootcamp2026.ui.theme.App
import kotlin.io.encoding.Base64

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_settings")

class AuthLocalDataSource() {

    companion object {
        fun getToken(): String? {
            return getInstance()._cacheToken
        }

        suspend fun getTokenSuspend(): String? {
            return getInstance().getTokenInternal()
        }

        @Volatile
        private var instance: AuthLocalDataSource? = null

        fun getInstance(): AuthLocalDataSource {
            return instance ?: synchronized(this) {
                instance ?: AuthLocalDataSource().also { instance = it }
            }
        }
    }

    private var isInit = false
    private var _cacheToken: String? = null
    private var _cacheUserId: Int? = null

    suspend fun setUserId(userId: Long) {
        _cacheUserId = userId?.toInt()
        App.context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId.toInt()
        }
    }

    suspend fun getUserId(): Int? {
        val id = App.context.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }.firstOrNull()
        return id
    }

    private suspend fun getTokenInternal(): String? {
        if (!isInit) {
            _cacheToken = try {
                App.context.dataStore.data.map { preferences ->
                    preferences[TOKEN]
                }.firstOrNull()
            } catch (e: Exception) {
                null
            }
            isInit = true
        }
        return _cacheToken
    }

    suspend fun setToken(login: String, password: String) {
        val decodePhrase = "$login:$password"
        val token = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
        _cacheToken = token

        App.context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun setTokenDirect(token: String) {
        _cacheToken = token
        App.context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun clearToken() {
        _cacheToken = null
        _cacheUserId = null
        App.context.dataStore.edit { preferences ->
            preferences.remove(TOKEN)
            preferences.remove(USER_ID)
        }
    }

    private val TOKEN = stringPreferencesKey("token")
    private val USER_ID = intPreferencesKey("user_id")
}