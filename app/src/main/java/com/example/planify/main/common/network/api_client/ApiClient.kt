package com.example.planify.main.common.network.api_client

import com.example.planify.core.network.ApiClientCore
import com.example.planify.core.network.ApiRequestContext
import com.example.planify.core.network.middleware.ApiClientMiddlewareChain
import com.example.planify.main.common.entities.ApiResponse
import com.example.planify.main.common.network.middlewares.ApiResponseParseMiddleware
import com.example.planify.main.common.network.middlewares.AppCodeValidatorMiddleware
import com.example.planify.main.common.network.middlewares.KtorExecuteMiddleware
import com.example.planify.main.common.network.middlewares.RetryRequestMiddleware
import com.example.planify.main.common.network.policies.app_code.AppCodeProcessingPolicy
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.serialization.serializer

open class ApiClient(
    httpClient: HttpClient,
    appCodeProcessingPolicy: AppCodeProcessingPolicy
) : ApiClientCore(
    httpClient
) {
    protected val ktorMiddleware = KtorExecuteMiddleware(httpClient)
    protected val appCodeValidatorMiddleware = AppCodeValidatorMiddleware(appCodeProcessingPolicy)
    protected val retryRequestMiddleware = RetryRequestMiddleware(3)

    open fun <T> setupMiddlewareChain(): ApiClientMiddlewareChain<ApiResponse<T>> {
        return ApiClientMiddlewareChain.Builder<ApiResponse<T>>()
            .add(ktorMiddleware)
            .add(retryRequestMiddleware)
            .add(ApiResponseParseMiddleware<T>())
            .add(appCodeValidatorMiddleware)
            .build()
    }

    suspend inline fun <reified T> request(
        crossinline build: HttpRequestBuilder.() -> Unit
    ): T? {
        val context = ApiRequestContext.Builder()
            .meta("serializer", serializer<T>())
            .build()

        val builder = HttpRequestBuilder()
        builder.apply(build)

        val chain = setupMiddlewareChain<T>()

        val response = chain.execute(builder, context)

        return response.data
    }

    suspend inline fun <reified T> requestNotNull(
        crossinline build: HttpRequestBuilder.() -> Unit
    ): T {
        return request<T>(build)!!
    }

    @Suppress("RedundantUnitExpression", "RedundantUnitReturnType")
    suspend inline fun requestUnit(
        crossinline build: HttpRequestBuilder.() -> Unit
    ): Unit {
        request<Unit>(build)
        return Unit
    }
}
