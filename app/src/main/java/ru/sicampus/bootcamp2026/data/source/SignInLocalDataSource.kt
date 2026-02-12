package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.domain.users.entities.UserEntity
import kotlin.io.encoding.Base64

object SignInLocalDataSource {

    private var isInit = false
    private var _cacheToken: String? = null

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
        App.context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences().also { preferences ->
                preferences[TOKEN] = token
            }
        }
    }

    fun clearToken() {
        _cacheToken = null
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val TOKEN = stringPreferencesKey("token")
}

