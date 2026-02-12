package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.PageDto

class MeetingApiService {
    private val client get() = Network.client

    suspend fun getMeetings(page: Int = 0, size: Int = 20): List<MeetingDto> {
        val response: PageDto<MeetingDto> = client.get("${Network.HOST}/api/meetings") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
        return response.content
    }

    suspend fun getMyMeetings(page: Int = 0, size: Int = 20): List<MeetingDto> {
        val response: PageDto<MeetingDto> = client.get("${Network.HOST}/api/meetings/my") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
        return response.content
    }

    suspend fun createMeeting(meeting: MeetingDto): MeetingDto {
        return client.post("${Network.HOST}/api/meetings") {
            contentType(ContentType.Application.Json)
            setBody(meeting)
        }.body()
    }
}
