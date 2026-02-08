package ru.innovationcampus.android.data.source

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthNetworkDataSource {
    suspend fun checkAuth(): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/users/login") {
                addAuthHeader()
            }
            result.status == HttpStatusCode.OK
        }
    }
}