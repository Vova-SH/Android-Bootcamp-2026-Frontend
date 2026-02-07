package ru.sicampus.bootcamp2026.android.data.source

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Network {
    //const val HOST = "http://10.0.2.2:8080" // вариант для эмулятора
    const val HOST = "http://192.168.1.66:8080" // вариант для телефона по usb, это у НастиВ
    //    const val HOST = "http://192.168.1.102:8080" // вариант для телефона по usb, это у НастиБ
    const val BASE_URL = "$HOST/api"



    val client by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KTOR", message)
                    }
                }
                level = LogLevel.INFO
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}
