package ru.sicampus.bootcamp2026.data.auth

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sicampus.bootcamp2026.data.network.AppApi
import ru.sicampus.bootcamp2026.data.network.StompManager
import java.util.concurrent.TimeUnit

object NetworkClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private var stompManager: StompManager? = null

    fun getStompManager(tokenStorage: TokenStorage): StompManager {
        if (stompManager == null) {
            stompManager = StompManager(tokenStorage)
        }
        return stompManager!!
    }

    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                if (response.code == 401) {
                    GlobalScope.launch {
                        SessionManager.triggerLogout()
                    }
                }
                response
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createAuthApi(): AuthApi = retrofit.create(AuthApi::class.java)
    fun createAppApi(): AppApi = retrofit.create(AppApi::class.java)
}