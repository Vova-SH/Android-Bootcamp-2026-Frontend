package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.sicampus.bootcamp2026.data.dto.PageDto
import ru.sicampus.bootcamp2026.data.dto.UserDto

class UserApiService {
    private val client get() = Network.client

    suspend fun getUsers(page: Int = 0, size: Int = 20, search: String? = null): List<UserDto> {
        val response: PageDto<UserDto> = client.get("${Network.HOST}/api/users") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
                search?.let { parameters.append("search", it) }
            }
        }.body()
        return response.content
    }
}
