package ru.sicampus.bootcamp2026.data.remote

import android.util.Log
import kotlinx.coroutines.*
import ru.sicampus.bootcamp2026.data.local.TokenDataStore
import ru.sicampus.bootcamp2026.domain.repository.AuthRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

/**
 * Менеджер для автоматического обновления access token'а до истечения
 * Token истекает каждые 5 минут, обновление происходит за 30 секунд до истечения
 */
@Singleton
class TokenRefreshManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) {

    companion object {
        private const val TAG = "TokenRefreshManager"
        // Обновляем за 30 секунд до истечения
        private const val REFRESH_BEFORE_EXPIRY_SECONDS = 30
        // Проверяем каждые 10 секунд
        private const val CHECK_INTERVAL_SECONDS = 10L
    }

    private var refreshJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    /**
     * Запустить менеджер обновления токенов
     */
    fun start() {
        Log.d(TAG, "Starting token refresh manager")
        if (refreshJob != null && refreshJob!!.isActive) {
            Log.d(TAG, "Token refresh manager already running")
            return
        }

        refreshJob = scope.launch {
            while (isActive) {
                try {
                    checkAndRefreshToken()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.e(TAG, "Error during token refresh check", e)
                }

                // Проверяем каждые 10 секунд
                delay(CHECK_INTERVAL_SECONDS * 1000)
            }
        }
    }

    /**
     * Остановить менеджер
     */
    fun stop() {
        Log.d(TAG, "Stopping token refresh manager")
        refreshJob?.cancel()
        refreshJob = null
    }

    /**
     * Проверить нужно ли обновлять токен и обновить его при необходимости
     */
    private suspend fun checkAndRefreshToken() {
        try {
            val expiresAtStr = tokenDataStore.getAccessTokenExpiresAtValue()
            val accessToken = tokenDataStore.getAccessTokenValue()

            if (expiresAtStr.isNullOrEmpty() || accessToken.isNullOrEmpty()) {
                Log.d(TAG, "No token data found, skipping refresh")
                return
            }

            val expiresAt = LocalDateTime.parse(expiresAtStr, DateTimeFormatter.ISO_DATE_TIME)
            val now = LocalDateTime.now()
            val secondsUntilExpiry = java.time.temporal.ChronoUnit.SECONDS.between(now, expiresAt)

            Log.d(TAG, "Token expires in $secondsUntilExpiry seconds")

            // Если осталось меньше 30 секунд (REFRESH_BEFORE_EXPIRY_SECONDS), обновляем
            if (secondsUntilExpiry <= REFRESH_BEFORE_EXPIRY_SECONDS) {
                Log.d(TAG, "Token expiring soon, refreshing now")
                refreshToken()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in checkAndRefreshToken", e)
        }
    }

    /**
     * Выполнить обновление токена
     */
    private suspend fun refreshToken() {
        try {
            Log.d(TAG, "Executing token refresh")
            val result = authRepository.refresh()

            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "Token refreshed successfully")
                }
                is Result.Error -> {
                    Log.e(TAG, "Failed to refresh token", result.exception)
                }
                is Result.Loading -> {
                    // Не ожидается
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during token refresh", e)
        }
    }
}

