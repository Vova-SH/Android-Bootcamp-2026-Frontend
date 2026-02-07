package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.runCatching
import ru.sicampus.bootcamp2026.android.data.dto.UserDTO

class UserInfoDataSource {
    suspend fun getUser(): Result<List<UserDTO>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person") {
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}