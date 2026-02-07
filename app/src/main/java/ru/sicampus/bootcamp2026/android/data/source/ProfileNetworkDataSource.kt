package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class ProfileNetworkDataSource {
    
    suspend fun getProfile(userId: Long): Result<PersonResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person/$userId") {
                addAuthHeader()
            }
            if (result.status == HttpStatusCode.OK) {
                result.body<PersonResponse>()
            } else {
                error("Failed to fetch profile: ${result.status}")
            }
        }
    }

    suspend fun updateProfile(
        userId: Long,
        fullName: String,
        department: String,
        position: String
    ): Result<PersonResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.put("${Network.HOST}/api/person/$userId") {
                addAuthHeader()
                setBody(
                    UpdateProfileRequest(
                        fullName = fullName,
                        department = department,
                        position = position
                    )
                )
            }
            if (result.status == HttpStatusCode.OK) {
                result.body<PersonResponse>()
            } else {
                error("Failed to update profile: ${result.status}")
            }
        }
    }
}

@Serializable
data class PersonResponse(
    val id: Long,
    val email: String,
    val fullName: String,
    val department: String,
    val position: String,
    val createdAt: String,
    val authorities: List<String>
)

@Serializable
data class UpdateProfileRequest(
    val fullName: String,
    val department: String,
    val position: String
)