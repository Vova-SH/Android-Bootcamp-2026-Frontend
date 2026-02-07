package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.LoginRequestDto
import ru.sicampus.bootcamp2026.data.dto.RegisterRequestDto
import ru.sicampus.bootcamp2026.data.dto.UserDto

class AuthNetworkDataSource {
    private val client: HttpClient = Network.client
    suspend fun login(login: String, password: String): Result<UserDto> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = client.post("${Network.HOST}/api/users/login") {
                    basicAuth(login, password)
                }

                when (result.status) {
                    HttpStatusCode.OK -> result.body<UserDto>()
                    HttpStatusCode.Unauthorized ->
                        throw Exception("Неверный логин или пароль")
                    else ->
                        throw Exception("Ошибка сервера: ${result.status}")
                }
            }
        }
//    suspend fun login(login: String, password: String): Result<UserDto> = withContext(Dispatchers.IO) {
//        runCatching {
//            val result = client.post("${Network.HOST}/api/users/login") {
//            setBody(LoginRequestDto(login, password))
//            }
//
//            when (result.status) {
//                HttpStatusCode.OK -> {
//                    result.body<UserDto>()
//                }
//                HttpStatusCode.Unauthorized -> {
//                    throw Exception("Неверный логин или пароль")
//                }
//                else -> {
//                    throw Exception("Ошибка сервера: ${result.status}")
//                }
//            }
//        }
//    }

    suspend fun checkAuth(): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.get("${Network.HOST}/api/users/login") {
                addAuthHeader()
            }
            result.status == HttpStatusCode.OK
        }
    }

    suspend fun register(
        login: String,
        password: String,
        name: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = client.post("${Network.HOST}/api/users/register") {
                setBody(RegisterRequestDto(
                    login = login,
                    password = password,
                    name = name,
                    lastName = lastName,
                    email = email,
                    phoneNumber = phoneNumber,
                    department = null,
                    position = null,
                    photoUrl = null
                ))
            }

            when (result.status) {
                HttpStatusCode.Created -> {
                    result.body<UserDto>()
                }
                HttpStatusCode.Conflict -> {
                    throw Exception("Пользователь с таким логином уже существует")
                }
                HttpStatusCode.BadRequest -> {
                    throw Exception("Неверные данные для регистрации")
                }
                else -> {
                    throw Exception("Ошибка сервера: ${result.status}")
                }
            }
        }
    }
}