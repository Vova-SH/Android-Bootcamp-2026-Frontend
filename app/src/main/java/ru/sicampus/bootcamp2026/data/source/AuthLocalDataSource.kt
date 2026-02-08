package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.ktor.client.request.header
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import ru.sicampus.bootcamp2026.App
import kotlinx.coroutines.flow.Flow

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object AuthLocalDataSource {

    private var isInit = false
    private var _cacheToken: String? = null

    private val TOKEN = stringPreferencesKey("token")

    // Используем App.instance вместо создания нового объекта
    private val dataStore: DataStore<Preferences> by lazy {
        (App.instance as Context).dataStore
    }

    suspend fun isUserAuthorized(): Boolean {
        return getToken() != null
    }

    suspend fun getToken(): String? {
        if (!isInit) {
            _cacheToken = dataStore.data
                .map { preferences -> preferences[TOKEN] }
                .firstOrNull()
            isInit = true
        }
        return _cacheToken
    }

    suspend fun setToken(jwtToken: String) {
        _cacheToken = jwtToken
        dataStore.edit { preferences ->
            preferences[TOKEN] = jwtToken
        }
    }

    suspend fun clearToken() {
        _cacheToken = null
        dataStore.edit { preferences ->
            preferences.remove(TOKEN)
        }
    }

    suspend fun HttpMessageBuilder.addAuthHeader() {
        val token = getToken()
        if (token != null) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}