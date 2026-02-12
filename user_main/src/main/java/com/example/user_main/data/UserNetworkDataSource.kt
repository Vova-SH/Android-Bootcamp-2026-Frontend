package com.example.user_main.data

import com.example.comon.ErrorResponseDto
import com.example.comon.GetUserResult
import com.example.comon.Network
import com.example.comon.UserDto
import com.example.comon.UserUpdateDto
import com.example.comon.User
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserNetworkDataSource @Inject constructor(
    private val network: Network
) {

    suspend fun getUserByToken(token: String): GetUserResult =
        withContext(Dispatchers.IO) {
            safeRequest {
                network.client.get("${network.HOST}/users/me") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

    suspend fun updateUser(
        token: String,
        user: User
    ): GetUserResult =
        withContext(Dispatchers.IO) {
            try {
                val response = network.client.put("${network.HOST}/users/profile") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        UserUpdateDto(
                            fullName = user.fullName,
                            department = user.department
                        )
                    )
                }

                if (response.status.isSuccess()) {
                    getUserByToken(token)
                } else {
                    extractError(response)
                }
            } catch (e: Exception) {
                GetUserResult.Error(e.message ?: "Ошибка сети")
            }
        }


    private suspend fun safeRequest(
        request: suspend () -> io.ktor.client.statement.HttpResponse
    ): GetUserResult {
        return try {
            val response = request()

            if (response.status.isSuccess()) {
                GetUserResult.Success(response.body<UserDto>())
            } else {
                extractError(response)
            }
        } catch (e: Exception) {
            GetUserResult.Error(e.message ?: "Ошибка сети")
        }
    }

    private suspend fun extractError(
        response: io.ktor.client.statement.HttpResponse
    ): GetUserResult {
        val message = runCatching {
            response.body<ErrorResponseDto>().message
        }.getOrNull()

        return GetUserResult.Error(
            message ?: "Ошибка (${response.status.value})"
        )
    }
}
