package ru.sicampus.bootcamp2026

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.remote.TokenRefreshService
import javax.inject.Inject

/**
 * Главный класс приложения с поддержкой Hilt
 */
@HiltAndroidApp
class MeetingPlannerApplication : Application() {

    @Inject
    lateinit var tokenRefreshService: TokenRefreshService

    companion object {
        private const val TAG = "MeetingPlannerApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Application created")

        // Инициализировать TokenRefreshService при старте приложения
        // Используем CoroutineScope вместо GlobalScope для безопасности
        CoroutineScope(Dispatchers.Default).launch {
            try {
                tokenRefreshService.initialize()
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing TokenRefreshService", e)
            }
        }
    }
}
