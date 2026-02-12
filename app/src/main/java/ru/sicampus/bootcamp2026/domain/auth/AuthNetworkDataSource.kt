package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.data.network.dto.EmployeeDto
import ru.sicampus.bootcamp2026.data.network.dto.EmployeesDto
import ru.sicampus.bootcamp2026.data.network.source.Network
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

class AuthNetworkDataSource {
    suspend fun login(email: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        runCatching {
            // Шаг 1: Получение токена
            val authResponse = Network.client.post("${Network.HOST}/api/Employee/auth") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("mail" to email, "password" to password))
            }

            if (authResponse.status != HttpStatusCode.OK) {
                throw Exception("Ошибка авторизации: ${authResponse.status}")
            }

            val tokenResponse = authResponse.body<AuthResponse>()
            val token = tokenResponse.token

            // Шаг 2: Получение данных пользователя
            val userResponse = Network.client.get("${Network.HOST}/api/Employee/getYou") {
                header("Authorization", "Bearer $token")
            }

            if (userResponse.status != HttpStatusCode.OK) {
                throw Exception("Не удалось загрузить данные пользователя")
            }

            val userDto = userResponse.body<EmployeeDto>()

            UserEntity(
                surname = userDto.surname ?: "",
                name = userDto.name ?: "",
                patronymic = userDto.patronymic,
                mail = userDto.mail ?: "",
                token = token
            )
        }
    }
}

@Serializable
private data class AuthResponse(
    @SerialName("token")
    val token: String
)

