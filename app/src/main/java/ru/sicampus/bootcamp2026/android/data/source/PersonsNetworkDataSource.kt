package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.sicampus.bootcamp2026.android.data.dto.PersonDto
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class PersonsNetworkDataSource {
    suspend fun getPerson(id: Long): Result<PersonDto> = runCatching {
        Network.client.get("${Network.HOST}/api/person/$id") {
            addAuthHeader()
        }.body()
    }

    suspend fun getPersonByEmail(email: String): Result<PersonResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person/email/$email") {
                addAuthHeader()
            }
            if (result.status == HttpStatusCode.OK) {
                result.body<PersonResponse>()
            } else {
                error("Person not found for email: $email")
            }
        }
    }

    suspend fun getAllPersons(page: Int = 0, size: Int = 20): Result<PersonPageResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person") {
                addAuthHeader()
                url {
                    parameters.append("page", page.toString())
                    parameters.append("size", size.toString())
                }
            }
            if (result.status == HttpStatusCode.OK) {
                result.body<PersonPageResponse>()
            } else {
                error("Failed to fetch persons")
            }
        }
    }
}

@Serializable
data class PersonPageResponse(
    val content: List<PersonResponse>,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int,
    val number: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)