package com.example.planify.main.common.network.di

import com.example.planify.main.common.network.api_client.ApiClient
import com.example.planify.main.common.network.ktor.KtorClient
import com.example.planify.main.common.network.policies.app_code.AppCodeProcessingPolicy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return KtorClient.client
    }

    @Provides
    @Singleton
    fun provideGeneralApiClient(
        httpClient: HttpClient,
        policy: AppCodeProcessingPolicy
    ): ApiClient {
        return ApiClient(httpClient, policy)
    }
}
