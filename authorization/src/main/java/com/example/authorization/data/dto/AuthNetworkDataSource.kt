package com.example.authorization.data.dto

import com.example.authorization.domain.entites.UserLoginEntity
import com.example.comon.ErrorResponseDto
import com.example.comon.LoginResponseDto
import com.example.comon.Network
import com.example.comon.RegisterResult
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthNetworkDataSource @Inject constructor(
    private val network: Network
) {

    suspend fun checkAuth(user: UserLoginEntity): RegisterResult =
        withContext(Dispatchers.IO) {
            try {
                val response = network.client.post("${network.HOST}/auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        UserLoginDto(
                            phoneNumber = user.phoneNumber,
                            password = user.password
                        )
                    )
                }

                if (response.status.isSuccess()) {
                    RegisterResult.Success(response.body())
                } else {
                    val message = runCatching {
                        response.body<ErrorResponseDto>().message
                    }.getOrNull()

                    RegisterResult.Error(
                        message ?: "Ошибка авторизации (${response.status.value})"
                    )
                }
            } catch (e: Exception) {
                RegisterResult.Error(e.message ?: "Ошибка сети")
            }
        }
}