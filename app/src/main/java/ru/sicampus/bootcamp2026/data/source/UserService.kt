package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.sicampus.bootcamp2026.data.dto.SpringPageDto
import ru.sicampus.bootcamp2026.data.dto.UserDto

class UserService {
    private val client = Network.client
    private val json = Network.json

    suspend fun getAllUsers(): List<UserDto> {
        return client.get("/api/users").body()
    }

    suspend fun getUsersPaginated(page: Int, size: Int): SpringPageDto<UserDto> {
        return client.get("/api/users/paginated") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getUserById(id: Long): UserDto {
        return client.get("/api/users/$id").body()
    }

    suspend fun getUserByEmail(email: String): UserDto {
        val response = client.get("/api/users/email/$email")

        return if (response.contentType()?.match(ContentType.Application.Json) == true) {
            response.body()
        } else {
            val textResponse = response.bodyAsText()
            try {
                json.decodeFromString<UserDto>(textResponse)
            } catch (e: Exception) {
                throw Exception("Unexpected server response format: $textResponse")
            }
        }
    }

    suspend fun updateUser(id: Long, userDto: UserDto): UserDto {
        return client.put("/api/users/$id") {
            setBody(userDto)
        }.body()
    }

    suspend fun deleteUser(id: Long) {
        client.delete("/api/users/$id")
    }
}