package ru.sicampus.bootcamp2026.data.source

import Network


import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthNetworkDataSource {
    suspend fun chekAuth(): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person/login") {
                addAuthHeader()
            }
            result.status == HttpStatusCode.OK
        }
    }
}






