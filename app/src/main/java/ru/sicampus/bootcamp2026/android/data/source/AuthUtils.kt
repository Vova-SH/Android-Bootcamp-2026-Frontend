package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

suspend fun HttpMessageBuilder.addAuthHeader() {
    val token = AuthLocalDataSource.getToken() ?: return
    header(HttpHeaders.Authorization, token)
}