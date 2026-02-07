package ru.sicampus.bootcamp2026.data.remote.interceptor

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.sicampus.bootcamp2026.data.local.TokenDataStore

/**
 * Интерцептор для автоматического добавления Bearer токена к запросам
 */
class AuthInterceptor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // Не добавляем токен для публичных эндпоинтов
        if (shouldSkipAuth(url)) {
            return chain.proceed(originalRequest)
        }

        // Для refresh эндпоинта используем refresh токен
        val token = if (url.contains("/auth/refresh")) {
            runBlocking { tokenDataStore.getRefreshToken().first() }
        } else {
            runBlocking { tokenDataStore.getAccessToken().first() }
        }

        // Если токена нет, выполняем запрос без него
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // Добавляем Bearer токен в заголовок Authorization
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }

    /**
     * Проверка, нужно ли пропустить аутентификацию для данного URL
     */
    private fun shouldSkipAuth(url: String): Boolean {
        return url.contains("/auth/login") ||
                url.contains("/auth/register") ||
                url.contains("/health")
    }
}

