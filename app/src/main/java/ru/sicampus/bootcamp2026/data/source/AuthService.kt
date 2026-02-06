package ru.sicampus.bootcamp2026.data.source

import android.util.Base64
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto

class AuthService {
    private val client = Network.client

    suspend fun register(registerDto: UserRegisterDto): UserDto {
        val response = client.post("/api/users/register") {
            setBody(registerDto)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Ошибка регистрации: ${response.status.value}")
        }

        return response.body()
    }

    suspend fun login(email: String, pass: String): UserDto {
        val credentials = "$email:$pass"
        val encodedCredentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        val authHeader = "Basic $encodedCredentials"

        val response = client.get("/api/users/login") {
            header(HttpHeaders.Authorization, authHeader)
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            throw Exception("Неверный email или пароль")
        }

        if (!response.status.isSuccess()) {
            throw Exception("Ошибка сервера: ${response.status.value}")
        }

        val user = response.body<UserDto>()

        SessionManager.saveSession(authHeader, user.id)
        return user
    }
}