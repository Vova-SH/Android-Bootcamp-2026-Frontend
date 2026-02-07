package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.data.dto.PageResponse
import ru.sicampus.bootcamp2026.android.data.dto.MeetingsCountByDateDto

class MeetingsNetworkDataSource {
    suspend fun getMyMeetingsForDay(
        date: String,
        page: Int,
        size: Int
    ): Result<PageResponse<MeetingResponse>> = runCatching {

        Network.client.get("${Network.HOST}/api/meetings/my/day") {
            addAuthHeader()
            parameter("date", date)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }
    suspend fun getMyMeetingsForWeek(
        start: String
    ): Result<List<MeetingsCountByDateDto>> = runCatching {

        Network.client.get("${Network.HOST}/api/meetings/my/week") {
            addAuthHeader()
            parameter("start", start)
        }.body()
    }
}
