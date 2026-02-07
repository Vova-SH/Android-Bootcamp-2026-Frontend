package com.teto.planner.di

import android.util.Base64
import android.util.Log
import com.teto.planner.data.local.CredentialsHolder
import com.teto.planner.data.remote.dto.ApiErrorDto
import com.teto.planner.data.remote.dto.ApiException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://teto-planner.fly.dev/"

    @Provides
    @Singleton
    fun provideHttpClient(credentialsHolder: CredentialsHolder): HttpClient {
        val jsonParams = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }

        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(jsonParams)
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        val errorBody = response.bodyAsText()

                        try {
                            val apiError = jsonParams.decodeFromString<ApiErrorDto>(errorBody)
                            val message = apiError.error ?: apiError.message ?: "Unknown API error"
                            throw ApiException(message, response.status.value)
                        } catch (e: Exception) {
                            if (e is ApiException) throw e

                            throw ApiException(
                                "Server error: ${response.status.value}. Body: $errorBody",
                                response.status.value
                            )
                        }
                    }
                }
            }

            defaultRequest {
                url(BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor-Network", message)
                    }
                }
                level = LogLevel.ALL
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                val path = request.url.encodedPath
                if (!path.contains("login") && !path.contains("register")) {
                    val creds = credentialsHolder.credentialsFlow.value

                    if (creds != null) {
                        val authString = "${creds.login}:${creds.pass}"
                        val encodedAuth = Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)
                        request.header(HttpHeaders.Authorization, "Basic $encodedAuth")
                    }
                }

                execute(request)
            }
        }
    }
}