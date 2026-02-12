package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
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

    suspend fun getMyMeetingsForMonth(
        month: String
    ): Result<List<MeetingsCountByDateDto>> = runCatching {
        Network.client.get("${Network.HOST}/api/meetings/my/month") {
            addAuthHeader()
            parameter("month", month)
        }.body()
    }
    suspend fun createMeeting(
        organizerId: Long,
        title: String,
        description: String,
        startAt: String,  // ISO 8601 format: "2026-02-07T09:00:00Z"
        endAt: String     // ISO 8601 format: "2026-02-07T10:00:00Z"
    ): Result<MeetingResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("${Network.HOST}/api/meetings") {
                addAuthHeader()
                setBody(
                    CreateMeetingRequest(
                        organizerId = organizerId,
                        title = title,
                        description = description,
                        startAt = startAt,
                        endAt = endAt
                    )
                )
            }
            if (result.status == HttpStatusCode.OK || result.status == HttpStatusCode.Created) {
                result.body<MeetingResponse>()
            } else {
                error("Failed to create meeting: ${result.status}")
            }
        }
    }
}

@Serializable
data class CreateMeetingRequest(
    val organizerId: Long,
    val title: String,
    val description: String,
    val startAt: String,
    val endAt: String
)
