package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.core.Constants
import ru.sicampus.bootcamp2026.data.dto.auth.RegisterRequest
import ru.sicampus.bootcamp2026.data.dto.user.UserDto

class AuthNetworkDataSource {

    // login
    suspend fun checkAuth(token: String?): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val response = ApiClient.client.get(Constants.LOGIN_ENDPOINT) {
                header(HttpHeaders.Authorization, token)
            }

            when (response.status) {
                HttpStatusCode.OK -> true
                HttpStatusCode.Unauthorized -> error("Логин или пароль неправильные")
                else -> error("Ошибка сервера: ${response.status} ${response.bodyAsText()}")
            }
        }
    }

    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response = ApiClient.client.post(Constants.REGISTER_ENDPOINT) {
                setBody(RegisterRequest(email, password, firstName, secondName))
            }

            when (response.status) {
                HttpStatusCode.OK -> response.body<UserDto>()


                HttpStatusCode.Conflict -> error("Такой email уже существует.")
                else -> error("Ошибка сервера: ${response.status}")
            }
        }
    }
}
