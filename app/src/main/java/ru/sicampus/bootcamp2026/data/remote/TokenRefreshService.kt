package ru.sicampus.bootcamp2026.data.remote

import android.util.Log
import kotlinx.coroutines.flow.first
import ru.sicampus.bootcamp2026.data.local.TokenDataStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Сервис для управления обновлением токенов при старте приложения и автентификации
 */
@Singleton
class TokenRefreshService @Inject constructor(
    private val tokenRefreshManager: TokenRefreshManager,
    private val tokenDataStore: TokenDataStore
) {

    companion object {
        private const val TAG = "TokenRefreshService"
    }

    /**
     * Инициализировать сервис при старте приложения
     * Запустить TokenRefreshManager если пользователь уже залогинен
     */
    suspend fun initialize() {
        try {
            val accessToken = tokenDataStore.getAccessToken().first()
            if (!accessToken.isNullOrEmpty()) {
                Log.d(TAG, "User is authenticated, starting token refresh manager")
                tokenRefreshManager.start()
            } else {
                Log.d(TAG, "User is not authenticated")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing TokenRefreshService", e)
        }
    }

    /**
     * Запустить менеджер обновления токенов после успешной аутентификации
     */
    fun onAuthenticationSuccess() {
        Log.d(TAG, "Authentication successful, starting token refresh manager")
        tokenRefreshManager.start()
    }

    /**
     * Остановить менеджер обновления токенов при выходе
     */
    fun onLogout() {
        Log.d(TAG, "Logout, stopping token refresh manager")
        tokenRefreshManager.stop()
    }
}

