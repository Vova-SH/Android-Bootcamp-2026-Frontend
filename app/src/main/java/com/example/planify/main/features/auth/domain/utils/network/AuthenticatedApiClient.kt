package com.example.planify.main.features.auth.domain.utils.network

import com.example.planify.core.network.middleware.ApiClientMiddlewareChain
import com.example.planify.main.common.entities.ApiResponse
import com.example.planify.main.common.network.api_client.ApiClient
import com.example.planify.main.common.network.middlewares.ApiResponseParseMiddleware
import com.example.planify.main.common.network.policies.app_code.AppCodeProcessingPolicy
import com.example.planify.main.features.auth.domain.AuthTokenManager
import com.example.planify.main.features.auth.domain.utils.network.middleware.AuthMiddleware
import com.example.planify.main.features.auth.domain.utils.network.middleware.AutoRefreshTokensMiddleware
import io.ktor.client.HttpClient
import jakarta.inject.Inject

open class AuthenticatedApiClient @Inject constructor(
    tokenManager: AuthTokenManager,
    httpClient: HttpClient,
    appCodeProcessingPolicy: AppCodeProcessingPolicy
) : ApiClient(httpClient, appCodeProcessingPolicy) {
    protected val authMiddleware = AuthMiddleware(tokenManager)
    protected val autoRefreshTokensMiddleware = AutoRefreshTokensMiddleware(tokenManager)

    override fun <T> setupMiddlewareChain(): ApiClientMiddlewareChain<ApiResponse<T>> {
        return ApiClientMiddlewareChain.Builder<ApiResponse<T>>()  // TODO: Use InsertBefore and InsertAfter
            .add(ktorMiddleware)
            .add(authMiddleware)
            .add(retryRequestMiddleware)
            .add(ApiResponseParseMiddleware<T>())
            .add(autoRefreshTokensMiddleware)
            .add(appCodeValidatorMiddleware)
            .build()
    }
}
