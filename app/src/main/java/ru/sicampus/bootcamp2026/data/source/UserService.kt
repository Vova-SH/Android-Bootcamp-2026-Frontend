package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import ru.sicampus.bootcamp2026.data.dto.UserDto

class UserService {
    private val client = Network.client

    suspend fun getUserById(id: Long): UserDto {
        return client.get("/api/users/$id") {
            SessionManager.authHeader?.let { header(HttpHeaders.Authorization, it) }
        }.body()
    }

    suspend fun updateUser(id: Long, userDto: UserDto): UserDto {
        return client.put("/api/users/$id") {
            SessionManager.authHeader?.let { header(HttpHeaders.Authorization, it) }
            setBody(userDto)
        }.body()
    }
}