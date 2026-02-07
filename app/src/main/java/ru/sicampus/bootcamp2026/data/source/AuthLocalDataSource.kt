package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sicampus.bootcamp2026.App
import kotlin.io.encoding.Base64

object AuthLocalDataSource {
    private var _cacheToken: String? = null

    suspend fun setToken(login: String, password: String){
        val decodePhrase = "$login:$password"
        val token = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
        _cacheToken = token
        App.context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[TOKEN] = token
            }
        }
    }
    fun getToken(): Flow<String?> {
        return App.context.dataStore.data.map { preferences ->
            preferences[TOKEN]
        }
    }
    suspend fun clearToken(){
        _cacheToken = null
        App.context.dataStore.edit { it.clear() }
    }
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    val TOKEN = stringPreferencesKey("token")
}