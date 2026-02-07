package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

object Network {

    const val HOST = "http://192.168.0.20:8080"

    lateinit var client: HttpClient

    fun init(appContext: Context) {
        client = HttpClient(Android) {
            //engine {
            //    context = appContext.applicationContext
            //}

            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}

//object Network {
//
//    const val HOST = "http://192.168.0.20:8080"
//
//    lateinit var client: HttpClient
//
//    fun init(context: Context) {
//        client = HttpClient(Android) {
//            engine {
//                this.context = context.applicationContext
//            }
//
//            install(ContentNegotiation) {
//                json(Json {
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                })
//            }
//
//            install(Logging) {
//                logger = Logger.DEFAULT
//                level = LogLevel.ALL
//            }
//
//            defaultRequest {
//                contentType(ContentType.Application.Json)
//            }
//        }
//    }
//}

//object Network {
//    const val HOST = "http://192.168.0.20:8080"
//    val client by lazy {
//        HttpClient(CIO) {
//            install(ContentNegotiation) {
//                json(
//                    Json {
//                        isLenient = true
//                        ignoreUnknownKeys = true
//                    }
//                )
//            }
//
//            install(Logging) {
//                logger = object : Logger {
//                    override fun log(message: String) {
//                        Log.d("KTOR", message)
//                    }
//                }
//            }
//            install(Logging) {
//                logger = Logger.DEFAULT
//                level = LogLevel.ALL
//            }
//            defaultRequest {
//                contentType(ContentType.Application.Json)
//            }
//        }
//    }
//}