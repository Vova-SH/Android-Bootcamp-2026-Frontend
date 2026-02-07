package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.sicampus.bootcamp2026.android.data.dto.PersonDto

class PersonsNetworkDataSource {

    suspend fun getPerson(id: Long): Result<PersonDto> = runCatching {
        Network.client.get("${Network.HOST}/api/person/$id") {
            addAuthHeader()
        }.body()
    }
}