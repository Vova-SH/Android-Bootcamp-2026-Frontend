package ru.sicampus.bootcamp2026.data.source.util

import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import kotlinx.coroutines.flow.firstOrNull
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource

suspend fun HttpMessageBuilder.addAuthHeader() {
    val token = AuthLocalDataSource.getToken().firstOrNull() ?: return
    header(HttpHeaders.Authorization, token)

}