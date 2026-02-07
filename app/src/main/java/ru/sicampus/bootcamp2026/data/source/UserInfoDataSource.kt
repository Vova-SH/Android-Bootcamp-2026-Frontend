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
    suspend fun getUsers(
        page: Int,
        size: Int,
    ): Result<PagingUserListDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/employees/paginated"){
                url {
                    parameter("page", page)
                    parameter("size", size)
                }
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK){
                error("Статус: ${result.status}")
            }
            result.body<PagingUserListDto>()
        }
    }
}