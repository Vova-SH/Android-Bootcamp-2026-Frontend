package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.RegisterUserDto

class AuthNetworkDataSource {
    suspend fun checkAuth(): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person/login") {
                addAuthHeader()
            }
            result.status == HttpStatusCode.OK
        }
    }

    suspend fun register(userRegisterUserDto: RegisterUserDto):
            Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("${Network.HOST}/api/person/register") {
                setBody(userRegisterUserDto)
            }
            result.status == HttpStatusCode.OK
        }
    }
}