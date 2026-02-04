package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.UserDto

class UserInfoDataSource {
    suspend fun getUser(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person")
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }

    suspend fun getCurrentUser(): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person") {
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }

    suspend fun updateCurrentUser(
        name: String?,
        phone: String?,
        email: String?,
        info: String?,
        photoUrl: String?
    ): Result<UserDto> = withContext(Dispatchers.IO)
    {
        runCatching {
            val userUpdates = UserDto(name, phone, email, info, photoUrl)
            val result = Network.client.post("${Network.HOST}/api/person") {
                contentType(ContentType.Application.Json)
                setBody(userUpdates)
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}