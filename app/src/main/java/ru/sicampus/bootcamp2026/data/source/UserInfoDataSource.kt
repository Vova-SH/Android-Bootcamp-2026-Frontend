package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.PagingUserListDto
import ru.sicampus.bootcamp2026.data.dto.UserDto

class UserInfoDataSource {
    suspend fun getUser(
        id: Int
    ): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/users") {
                url {
                    parameter("id", id)
                }
                addSignInHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }

    suspend fun getUsers(
        page: Int,
        size: Int
    ): Result<PagingUserListDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/users/paginated") {
                url {
                    parameter("pageNumber", page)
                    parameter("pageSize", size)
                }
                addSignInHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}