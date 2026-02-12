package com.example.planify.main.common.network.middlewares

import com.example.planify.core.data.serializers.jsonCore
import com.example.planify.core.network.ApiRequestContext
import com.example.planify.core.network.middleware.ApiClientMiddleware
import com.example.planify.main.common.entities.ApiResponse
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement

class ApiResponseParseMiddleware<T> : ApiClientMiddleware<HttpRequestBuilder, ApiResponse<T>> {
    override suspend fun proceed(
        context: ApiRequestContext,
        next: suspend (HttpRequestBuilder) -> Any?,
        input: HttpRequestBuilder
    ): ApiResponse<T> {
        val httpResponse = next(input) as HttpResponse
        val text = httpResponse.bodyAsText()

        val apiResponseJson = jsonCore.decodeFromString<ApiResponse<JsonElement>>(text)

        val data: T? = apiResponseJson.data?.let {
            val serializer = context.getFromMeta<KSerializer<T>>("serializer") ?: throw IllegalStateException("Serializer for type T not found in context")
            jsonCore.decodeFromJsonElement(serializer, apiResponseJson.data)
        }

        return ApiResponse(
            ok = apiResponseJson.ok,
            appCode = apiResponseJson.appCode,
            message = apiResponseJson.message,
            data = data
        )
    }
}
