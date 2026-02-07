package ru.sicampus.bootcamp2026.data.repository


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.sicampus.bootcamp2026.data.dto.user.UserDto
import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
import ru.sicampus.bootcamp2026.data.dto.user.UserUpdateDto
import ru.sicampus.bootcamp2026.data.source.ApiClient


interface UserRepository {
    suspend fun getUserById(id: Long): Result<UserDto>
    suspend fun searchUsers(search: String): Result<List<UserMiniDto>>
    suspend fun updateUser(userUpdateDto: UserUpdateDto): Result<UserDto>
}

class UserRepositoryImpl(
    private val client: HttpClient = ApiClient.client
) : UserRepository {

    override suspend fun getUserById(id: Long): Result<UserDto> {
        return try {
            // TODO: Заменить на реальный endpoint
            val response = client.get("http://10.0.2.2:8080/api/v1/user/$id")
            val userDto = response.body<UserDto>()
            Result.success(userDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(search: String): Result<List<UserMiniDto>> {
        return try {
            // TODO: Заменить на реальный endpoint
            val response = client.get("http://10.0.2.2:8080/api/v1/user/search?search=$search")
            val users = response.body<List<UserMiniDto>>()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(userUpdateDto: UserUpdateDto): Result<UserDto> {
        return try {
            // TODO: Заменить на реальный endpoint
            val response = client.post("http://10.0.2.2:8080/api/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(userUpdateDto)
            }
            val userDto = response.body<UserDto>()
            Result.success(userDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}