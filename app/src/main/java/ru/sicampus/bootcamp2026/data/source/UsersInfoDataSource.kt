package ru.sicampus.bootcamp2026.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import ru.sicampus.bootcamp2026.data.dto.PagingUserListDTO
import ru.sicampus.bootcamp2026.data.dto.user.UserDTO

class UsersInfoDataSource {
    suspend fun getUsers(
        page: Int,
        size: Int
    ): Result<PagingUserListDTO> = withContext(Dispatchers.IO) {
        runCatching {
            val token = AuthLocalDataSource.token?: error("Not authorized")
            val result = Network.client.get("${Network.HOST}/api/users/paginated") {
                url {
                    parameter("page", page)
                    parameter("size", size)
                }
                header(HttpHeaders.Authorization, "Basic $token")
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()


        }
    }

    suspend fun getUserByEmail(email: String?): Result<UserDTO> = withContext(Dispatchers.IO) {
        runCatching {
            val token = AuthLocalDataSource.token ?: error("Not authorized")

            val result = Network.client.get("${Network.HOST}/api/users/email/$email") {
                header(HttpHeaders.Authorization, "Basic $token")
            }

            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }

            result.body<UserDTO>()
        }
    }

}