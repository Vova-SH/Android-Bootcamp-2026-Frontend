package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.MeetingInputDto
import ru.sicampus.bootcamp2026.data.dto.MemberDto
import ru.sicampus.bootcamp2026.data.dto.SpringPageDto

class MeetingService {
    private val client = Network.client

    suspend fun getAllMeetings(): List<MeetingDto> {
        return client.get("/api/meeting/").body()
    }

    suspend fun getMeetingsPaginated(page: Int, size: Int): SpringPageDto<MeetingDto> {
        return client.get("/api/meeting/paginated") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getSchedule(): List<MeetingDto> {
        return client.get("/api/meeting/schedule").body()
    }

    suspend fun getMeetingById(id: Long): MeetingDto {
        return client.get("/api/meeting/$id").body()
    }

    suspend fun getMeetingMembers(id: Long): List<MemberDto> {
        return client.get("/api/meeting/$id/members").body()
    }

    suspend fun createMeeting(input: MeetingInputDto): MeetingDto {
        val response = client.post("/api/meeting/book") {
            setBody(input)
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorText = response.bodyAsText()
            throw Exception(errorText.ifBlank { "Ошибка создания: ${response.status.value}" })
        }
    }

    suspend fun updateMeeting(id: Long, input: MeetingInputDto): MeetingDto {
        val response = client.put("/api/meeting/$id") {
            setBody(input)
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorText = response.bodyAsText()
            throw Exception(errorText.ifBlank { "Ошибка обновления: ${response.status.value}" })
        }
    }

    suspend fun deleteMeeting(meetingId: Long) {
        val response = client.delete("/api/meeting/$meetingId")
        if (!response.status.isSuccess()) {
            val errorText = response.bodyAsText()
            throw Exception(errorText.ifBlank { "Ошибка удаления: ${response.status.value}" })
        }
    }
}