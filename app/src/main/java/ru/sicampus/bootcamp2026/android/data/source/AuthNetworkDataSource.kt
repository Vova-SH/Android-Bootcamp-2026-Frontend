package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class AuthNetworkDataSource {
    suspend fun checkAuth(): Result<PersonResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person/login") {
                addAuthHeader()
            }
            if (result.status == HttpStatusCode.OK) {
                val loginResponse = result.body<LoginResponse>()
                loginResponse.principal
            } else {
                error("Auth check failed: ${result.status}")
            }
        }
    }
}

@Serializable
data class LoginResponse(
    val name: String,
    val authorities: List<String>,
    val principal: PersonResponse
)