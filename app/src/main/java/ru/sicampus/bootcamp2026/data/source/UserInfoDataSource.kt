package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.PagingUserListDto
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.home.entities.InvitationEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserRequest

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

    suspend fun changeUser(
        id: Int,
        email: String,
        fullname: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val requestBody = UserRequest(
                id = id,
                email = email,
                fullName = fullname
            )

            val result = Network.client.put("${Network.HOST}/api/employees") {
                addAuthHeader()
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            result.body<Unit>()
        }
    }
}