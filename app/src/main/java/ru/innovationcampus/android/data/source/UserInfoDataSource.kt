package ru.innovationcampus.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.innovationcampus.android.data.dto.PagingUserListDto

class UserInfoDataSource {
    suspend fun getUser(
        page: Int,
        size: Int
    ): Result<PagingUserListDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/users/paginated") {
                url {
                    parameter("page", page)
                    parameter("size", size)
                }
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}