package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.UserDto

open class UserInfoDataSource {
    open suspend fun getUser(): Result <List<UserDto>> = withContext(Dispatchers.IO){
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/person")
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()

        }

        }
}