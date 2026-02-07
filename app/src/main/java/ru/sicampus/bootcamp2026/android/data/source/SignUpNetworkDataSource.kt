package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class SignUpNetworkDataSource {
    suspend fun registerUser(
        email: String,
        password: String,
        fullName: String,
        department: String,
        position: String
    ): Result<PersonResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("${Network.HOST}/api/person/register") {
                setBody(
                    RegisterRequest(
                        email = email,
                        password = password,
                        fullName = fullName,
                        department = department,
                        position = position
                    )
                )
            }
            if (result.status == HttpStatusCode.OK || result.status == HttpStatusCode.Created) {
                result.body<PersonResponse>()
            } else {
                error("Registration failed: ${result.status}")
            }
        }
    }
}

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val department: String,
    val position: String
)