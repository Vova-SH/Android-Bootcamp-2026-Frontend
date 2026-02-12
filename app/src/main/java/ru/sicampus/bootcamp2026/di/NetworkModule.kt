package ru.sicampus.bootcamp2026.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.sicampus.bootcamp2026.data.local.TokenDataStore
import ru.sicampus.bootcamp2026.data.remote.api.AuthApi
import ru.sicampus.bootcamp2026.data.remote.api.InvitationApi
import ru.sicampus.bootcamp2026.data.remote.api.MeetingApi
import ru.sicampus.bootcamp2026.data.remote.api.ProfileApi
import ru.sicampus.bootcamp2026.data.remote.interceptor.AuthInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Модуль Hilt для предоставления зависимостей сети
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     Запустить докер компоуз из бэка и узнать айпишника ноута потом вписать сюда (работает на одной сети устройство + ноут)
     */
    private const val BASE_URL = "http://192.168.50.31:8080/"  // IP вашего ноутбука
    private const val TIMEOUT_SECONDS = 30L

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): TokenDataStore {
        return TokenDataStore(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenDataStore: TokenDataStore
    ): AuthInterceptor {
        return AuthInterceptor(tokenDataStore)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMeetingApi(retrofit: Retrofit): MeetingApi {
        return retrofit.create(MeetingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInvitationApi(retrofit: Retrofit): InvitationApi {
        return retrofit.create(InvitationApi::class.java)
    }
}

