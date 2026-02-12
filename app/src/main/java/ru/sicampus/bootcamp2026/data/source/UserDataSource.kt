package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.core.Constants
import ru.sicampus.bootcamp2026.data.dto.PageDto
import ru.sicampus.bootcamp2026.data.dto.user.UserDto
import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
import ru.sicampus.bootcamp2026.data.dto.user.UserUpdateDto

class UserDataSource {

    suspend fun getUserById(userId: Long): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response = ApiClient.client.get(Constants.GET_BY_ID_ENDPOINT + "/$userId")

            when (response.status) {
                HttpStatusCode.OK -> response.body<UserDto>()

                HttpStatusCode.NotFound -> error("Такого пользователя не существует.")
                else -> error("Ошибка сервера: ${response.status}")
            }
        }
    }

    suspend fun updateUser(
        userId: Long,
        firstName: String,
        secondName: String,
        description: String?,
        position: String?,
        department: String?
    ): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response = ApiClient.client.post(Constants.UPDATE_USER_ENDPOINT) {
                setBody(UserUpdateDto(userId, firstName, secondName, description, position, department))
            }

            when (response.status) {
                HttpStatusCode.OK -> response.body<UserDto>()

                HttpStatusCode.BadRequest -> error("Имя или фамилия не могут быть пустыми")
                HttpStatusCode.NotFound -> error("Пользователь не найден")
                else -> error("Ошибка сервера: ${response.status}")
            }
        }
    }

    suspend fun searchUsers(
        searchQuery: String,
        page: Int = 0,
        size: Int = 10
    ): Result<List<UserMiniDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = ApiClient.client.get(Constants.SEARCH_USERS_ENDPOINT) {
                url {
                    parameters.append("search", searchQuery)
                    parameters.append("page", page.toString())
                    parameters.append("size", size.toString())
                }
            }

            when (response.status) {
                HttpStatusCode.OK ->  response.body<PageDto<UserMiniDto>>().content
                HttpStatusCode.BadRequest -> error("Некорректные параметры поиска")
                else -> error("Ошибка сервера: ${response.status}")
            }
        }
    }
}