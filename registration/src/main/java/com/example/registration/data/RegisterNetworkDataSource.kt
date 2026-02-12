package com.example.registration.data

import com.example.comon.ErrorResponseDto
import com.example.comon.LoginResponseDto
import com.example.comon.Network
import com.example.comon.RegisterResult
import com.example.comon.UserDto
import com.example.comon.UserEntity
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterNetworkDataSource @Inject constructor(
    private val network: Network
) {


    suspend fun registerUser(user: UserEntity): RegisterResult =
        withContext(Dispatchers.IO) {
            try {
                val response = network.client.post("${network.HOST}/users/registration") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        UserDto(
                            fullName = user.fullName,
                            phoneNumber = user.phoneNumber,
                            department = user.department,
                            password = user.password
                        )
                    )
                }

                if (response.status.isSuccess()) {
                    val body = response.body<LoginResponseDto>()
                    RegisterResult.Success(body)
                } else {
                    val errorMessage = runCatching {
                        response.body<ErrorResponseDto>().message
                    }.getOrNull()

                    RegisterResult.Error(
                        errorMessage ?: "Ошибка регистрации (${response.status.value})"
                    )
                }
            } catch (e: Exception) {
                RegisterResult.Error(
                    e.message ?: "Неизвестная ошибка"
                )
            }
        }


}