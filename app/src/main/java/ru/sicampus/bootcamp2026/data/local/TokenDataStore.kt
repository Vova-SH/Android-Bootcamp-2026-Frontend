package ru.sicampus.bootcamp2026.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore для хранения токенов авторизации
 */
class TokenDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val ACCESS_TOKEN_EXPIRES_AT_KEY = stringPreferencesKey("access_token_expires_at")
        private val REFRESH_TOKEN_EXPIRES_AT_KEY = stringPreferencesKey("refresh_token_expires_at")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
    }

    /**
     * Сохранение access токена
     */
    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    /**
     * Сохранение refresh токена
     */
    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }

    /**
     * Сохранение всех токенов и данных пользователя
     */
    suspend fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        accessTokenExpiresAt: String,
        refreshTokenExpiresAt: String,
        userId: String,
        username: String,
        email: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[ACCESS_TOKEN_EXPIRES_AT_KEY] = accessTokenExpiresAt
            preferences[REFRESH_TOKEN_EXPIRES_AT_KEY] = refreshTokenExpiresAt
            preferences[USER_ID_KEY] = userId
            preferences[USERNAME_KEY] = username
            preferences[EMAIL_KEY] = email
        }
    }

    /**
     * Получение access токена
     */
    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }

    /**
     * Получение refresh токена
     */
    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }

    /**
     * Получение времени истечения access токена
     */
    fun getAccessTokenExpiresAt(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_EXPIRES_AT_KEY]
        }
    }

    /**
     * Получение ID пользователя
     */
    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    /**
     * Получение username
     */
    fun getUsername(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USERNAME_KEY]
        }
    }

    /**
     * Получение email
     */
    fun getEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[EMAIL_KEY]
        }
    }

    /**
     * Очистка всех данных
     */
    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

