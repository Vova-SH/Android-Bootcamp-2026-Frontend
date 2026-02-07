package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.data.dto.UserDto
import kotlin.io.encoding.Base64

object AuthLocalDataSource {
    private var isInit = false
    private var _cacheToken: String? = null

    suspend fun getToken(): String? {
        if(!isInit){
            _cacheToken = App.context.dataStore.data.map { preferences ->
                preferences[TOKEN]
            }.firstOrNull()
            isInit = true
        }
        return _cacheToken
    }

    suspend fun setToken(login: String, password: String){
        val decodePhrase = "$login:$password"
        val token = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
        _cacheToken = token
        App.context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().also { preferences ->
                preferences[TOKEN] = token
            }
        }
    }

    fun clearToken(){
        _cacheToken = null
    }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val TOKEN = stringPreferencesKey("token")




    private var _currentUser: UserDto? = null

    suspend fun saveUser(user: UserDto) {
        _currentUser = user
        App.context.dataStore.edit { preferences ->
            preferences[USER_DATA] = Json.encodeToString(user)
        }
    }

    suspend fun getCurrentUser(): UserDto? {
        if (_currentUser == null) {
            _currentUser = App.context.dataStore.data.map { preferences ->
                    preferences[USER_DATA]?.let { json ->
                        Json.decodeFromString<UserDto>(json)
                    }
                }
                .firstOrNull()
        }
        return _currentUser
    }

    fun clearUser() {
        _currentUser = null
    }

    private val USER_DATA = stringPreferencesKey("current_user")
}