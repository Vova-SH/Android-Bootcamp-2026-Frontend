package com.example.planify.main.common.network.ktor

import com.example.planify.BuildConfig
import com.example.planify.core.data.serializers.jsonCore
import com.example.planify.main.common.network.ktor.plugins.KtorResponseLoggerPlugin
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json

object KtorClient {
    val client = HttpClient() {
        install(KtorResponseLoggerPlugin.plugin)

        install(ContentNegotiation) {
            json(jsonCore)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }

        defaultRequest {
            host = BuildConfig.API_HOST
            url { protocol = URLProtocol.HTTP }
            headers {
                set(HttpHeaders.ContentType, "application/json")
            }
        }
    }
}