package ru.sicampus.bootcamp2026.data.source

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Network {
    private const val BASE_URL = "http://192.168.1.195:8080"

    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
    }

    val client by lazy {
        HttpClient(CIO) {
            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
                TokenStorage.accessToken?.let { token ->
                    header(HttpHeaders.Authorization, token)
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Network", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
}