package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import android.util.Log
import ru.sicampus.bootcamp2026.data.dto.RegistrationRequestDto
import ru.sicampus.bootcamp2026.data.dto.UserDto

class AuthApiService {
    private val client = Network.client

    suspend fun checkAuth(): Boolean {
        return try {
            val response = client.get("${Network.HOST}/api/users/me")
            if (response.status == HttpStatusCode.Unauthorized) {
                return false
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            Log.e("AuthApiService", "checkAuth error", e)
            false
        }
    }

    suspend fun getCurrentUser(): UserDto {
        return client.get("${Network.HOST}/api/users/me").body()
    }

    suspend fun updateUser(fullName: String): UserDto {
        return client.post("${Network.HOST}/api/users/update") {
            setBody(mapOf("fullName" to fullName))
        }.body()
    }

    suspend fun register(request: RegistrationRequestDto): UserDto {
        return client.post("${Network.HOST}/api/users/register") {
            setBody(request)
        }.body()
    }
}
