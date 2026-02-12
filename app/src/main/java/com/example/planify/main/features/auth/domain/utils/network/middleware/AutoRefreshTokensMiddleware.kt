package com.example.planify.main.features.auth.domain.utils.network.middleware

import android.util.Log
import com.example.planify.core.exceptions.UnauthenticatedAppError
import com.example.planify.core.network.ApiRequestContext
import com.example.planify.core.network.middleware.ApiClientMiddleware
import com.example.planify.main.common.entities.ApiResponse
import com.example.planify.main.features.auth.domain.AuthTokenManager
import io.ktor.client.request.HttpRequestBuilder

class AutoRefreshTokensMiddleware(
    private val tokenManager: AuthTokenManager
) : ApiClientMiddleware<HttpRequestBuilder, ApiResponse<*>> {
    private suspend fun tryToRefreshTokens(): Result<Unit> = tokenManager.refreshTokens()

    override suspend fun proceed(
        context: ApiRequestContext,
        next: suspend (HttpRequestBuilder) -> Any?,
        input: HttpRequestBuilder
    ): ApiResponse<*> {
        val response = next(input) as ApiResponse<*>

        if (!(response.appCode in 3005..3008 || response.appCode in 3010..3012)) return response

        try {
            tryToRefreshTokens().getOrThrow()
        } catch (error: UnauthenticatedAppError) {
            Log.w(this::class.simpleName, "Failed to refresh tokens, cancelling request ${input.method} - ${input.url}: ${error::class.simpleName}: ${error.message}")
        }

        val retryInput = HttpRequestBuilder().takeFrom(input)
        return next(retryInput) as ApiResponse<*>
    }
}
