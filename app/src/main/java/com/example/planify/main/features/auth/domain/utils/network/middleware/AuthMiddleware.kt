package com.example.planify.main.features.auth.domain.utils.network.middleware

import com.example.planify.core.network.ApiRequestContext
import com.example.planify.core.network.middleware.ApiClientMiddleware
import com.example.planify.main.features.auth.domain.AuthTokenManager
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class AuthMiddleware(
    private val tokenManager: AuthTokenManager
) : ApiClientMiddleware<HttpRequestBuilder, HttpResponse> {
    private val authTokenType = "Bearer"

    private fun constructHeader(accessToken: String): String {
        return "$authTokenType $accessToken"
    }

    override suspend fun proceed(
        context: ApiRequestContext,
        next: suspend (HttpRequestBuilder) -> Any?,
        input: HttpRequestBuilder
    ): HttpResponse {
        val tokens = tokenManager.getTokenPair()

        input.apply {
            headers {
                set(HttpHeaders.Authorization, constructHeader(tokens.accessToken))
            }
        }

        return next(input) as HttpResponse
    }
}
