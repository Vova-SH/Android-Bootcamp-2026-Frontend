package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import android.util.Log

object Network {
    const val HOST = "http://10.0.2.2:8080"
    val client by lazy {
        HttpClient(CIO){
            install(ContentNegotiation){
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging){
                logger = object: Logger {
                    override fun log(message: String) {
                        Log.d("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest { 
                contentType(ContentType.Application.Json)
                AuthLocalDataSource.token?.let {
                    Log.d("Network", "Adding Authorization header: $it")
                    header(HttpHeaders.Authorization, it)
                } ?: Log.d("Network", "No token found in AuthLocalDataSource")
            }
        }
    }
}