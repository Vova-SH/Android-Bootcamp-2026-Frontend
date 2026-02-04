package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.CreateMeetingDTO
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.UserDto

class MeetingInfoDataSource {
    suspend fun getMeeting(): Result<List<MeetingDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/meeting")
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }

    suspend fun createMeeting(
        name: String?,
        startTime: Byte?,
        endTime: Byte?,
        date: String?,
        participants: List<Int>?
        ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val meeting = CreateMeetingDTO(name, startTime, endTime, date, participants)
            val result = Network.client.post("${Network.HOST}/api/meeting") {
                setBody(meeting)
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}
