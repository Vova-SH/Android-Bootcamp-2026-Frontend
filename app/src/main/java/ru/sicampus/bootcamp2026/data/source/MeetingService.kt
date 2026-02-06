package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.MeetingInputDto
import ru.sicampus.bootcamp2026.data.dto.SpringPageDto

class MeetingService {
    private val client = Network.client

    suspend fun getAllMeetings(): List<MeetingDto> {
        return client.get("/api/meeting/") {
            SessionManager.authHeader?.let { header(HttpHeaders.Authorization, it) }
        }.body()
    }

    suspend fun getMeetingsPaginated(page: Int, size: Int): SpringPageDto<MeetingDto> {
        return client.get("/api/meeting/paginated") {
            SessionManager.authHeader?.let { header(HttpHeaders.Authorization, it) }
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun createMeeting(userId: Long, input: MeetingInputDto): MeetingDto {
        return client.post("/api/meeting/book/$userId") {
            SessionManager.authHeader?.let { header(HttpHeaders.Authorization, it) }
            setBody(input)
        }.body()
    }

    suspend fun deleteMeeting(meetingId: Long) {
        client.delete("/api/meeting/$meetingId") {
            SessionManager.authHeader?.let { header(HttpHeaders.Authorization, it) }
        }
    }
}