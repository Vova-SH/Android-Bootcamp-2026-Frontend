package com.example.planify.main.common.network.middlewares

import com.example.planify.core.network.ApiRequestContext
import com.example.planify.core.network.middleware.ApiClientMiddleware
import com.example.planify.core.utils.retrying
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse

class RetryRequestMiddleware(
    val retryCount: Int
) : ApiClientMiddleware<HttpRequestBuilder, HttpResponse> {
    override suspend fun proceed(
        context: ApiRequestContext,
        next: suspend (HttpRequestBuilder) -> Any?,
        input: HttpRequestBuilder
    ): HttpResponse {
        return retrying(
            attempts = retryCount,
            initialDelayMs = 200,
            backoffFactor = 2.0, tag = this::class.simpleName!!
        ) {
            next(input) as HttpResponse
        }
    }
}
