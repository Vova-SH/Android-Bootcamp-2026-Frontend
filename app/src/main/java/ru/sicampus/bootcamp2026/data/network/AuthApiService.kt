package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import android.util.Log
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.sicampus.bootcamp2026.data.dto.RegistrationRequestDto
import ru.sicampus.bootcamp2026.data.dto.UserDto

class AuthApiService {
    private val client get() = Network.client

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

    suspend fun uploadPfp(imageBytes: ByteArray, fileName: String): UserDto {
        return client.post("${Network.HOST}/api/users/upload-pfp") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", imageBytes, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        })
                    }
                )
            )
        }.body()
    }

    suspend fun updateUser(fullName: String): UserDto {
        return client.post("${Network.HOST}/api/users/update") {
            contentType(ContentType.Application.Json)
            setBody(UserDto(fullName = fullName))
        }.body()
    }

    suspend fun register(request: RegistrationRequestDto): UserDto {
        return client.post("${Network.HOST}/api/users/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
