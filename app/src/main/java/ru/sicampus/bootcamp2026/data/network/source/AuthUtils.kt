package ru.sicampus.bootcamp2026.data.network.source

import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import ru.sicampus.bootcamp2026.token

suspend fun HttpMessageBuilder.addAuthHeader() {
    val token = token
    header(HttpHeaders.Authorization, token)
}
