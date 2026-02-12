package com.example.planify.main.common.network.middlewares

import com.example.planify.core.network.ApiRequestContext
import com.example.planify.core.network.middleware.ApiClientMiddleware
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse

class KtorExecuteMiddleware(
    private val client: HttpClient
) : ApiClientMiddleware<HttpRequestBuilder, HttpResponse> {
    override suspend fun proceed(
        context: ApiRequestContext,
        next: suspend (HttpRequestBuilder) -> Any?,
        input: HttpRequestBuilder
    ): HttpResponse {
        return client.request(input)
    }
}
