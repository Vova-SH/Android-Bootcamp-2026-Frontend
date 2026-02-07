package ru.sicampus.bootcamp2026.data.source


import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.user.UserRegisterDTO
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import ru.sicampus.bootcamp2026.data.dto.user.UserDTO


class AuthNetworkDataSource {
    suspend fun checkAuth(token: String): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val token = AuthLocalDataSource.token ?: error("Not authorized")
            val result = Network.client.get("${Network.HOST}/api/users/login") {
                header(HttpHeaders.Authorization, "Basic $token")
            }

            result.status == HttpStatusCode.OK


        }.getOrElse { false }
    }

    suspend fun registration(user: UserRegisterDTO): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val token = AuthLocalDataSource.token ?: error("Not authorized")
            val result = Network.client.post("${Network.HOST}/api/users/register") {
                header(HttpHeaders.Authorization, "Basic $token")
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            result.status == HttpStatusCode.Created || result.status == HttpStatusCode.OK
        }.getOrElse { false }
    }



}