package com.example.planify.main.common.network.middlewares

import com.example.planify.core.network.ApiRequestContext
import com.example.planify.core.network.middleware.ApiClientMiddleware
import com.example.planify.main.common.entities.ApiResponse
import com.example.planify.main.common.network.policies.app_code.AppCodeProcessingPolicy
import io.ktor.client.request.HttpRequestBuilder

class AppCodeValidatorMiddleware(
    private val policy: AppCodeProcessingPolicy
) : ApiClientMiddleware<HttpRequestBuilder, ApiResponse<*>> {
    override suspend fun proceed(
        context: ApiRequestContext,
        next: suspend (HttpRequestBuilder) -> Any?,
        input: HttpRequestBuilder
    ): ApiResponse<*> {
        val response = next(input) as ApiResponse<*>
        policy.process(response.appCode, response.message)
        return response
    }
}
