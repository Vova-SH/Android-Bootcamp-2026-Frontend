package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.PagingMeetingListDto
import kotlin.Result

class MeetingInfoDataSource {
    suspend fun getPlannedMeetings(
        page: Int,
        size: Int,
        userId: Int
    ): Result<PagingMeetingListDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/meetings/planned/paginated") {
                url {
                    parameter("pageNumber", page)
                    parameter("pageSize", size)
                    parameter("userId", userId)
                }
                addSignInHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }

    suspend fun getMeetings(
        page: Int,
        size: Int
    ): Result<PagingMeetingListDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/meetings/paginated") {
                url {
                    parameter("pageNumber", page)
                    parameter("pageSize", size)
                }
                addSignInHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}
