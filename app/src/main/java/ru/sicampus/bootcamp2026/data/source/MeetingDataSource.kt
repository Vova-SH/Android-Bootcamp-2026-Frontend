package ru.sicampus.bootcamp2026.data.source


import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.domain.entities.InvitationStatus
import ru.sicampus.bootcamp2026.data.dto.invitation.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.invitation.InvitationRespondDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingCreateDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto
import java.time.LocalDate

class MeetingDataSource {
    suspend fun createMeeting(meetingData: MeetingCreateDto): Result<MeetingDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = ApiClient.client.post("meetings/create") {
                setBody(meetingData)
            }
            result.body<MeetingDto>()
        }
    }

    suspend fun getMeetingById(id: Long): Result<MeetingDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = ApiClient.client.get("meetings/$id")
            result.body<MeetingDto>()
        }
    }

    suspend fun getDaySchedule(day: LocalDate): Result<List<MeetingMiniDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = ApiClient.client.get("meetings/schedule/day?day=$day")
            result.body<List<MeetingMiniDto>>()
        }
    }

    suspend fun getWeekSchedule(year: Int, week: Int): Result<Map<String, List<MeetingMiniDto>>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = ApiClient.client.get("meetings/schedule/week?year=$year&week=$week")
                result.body<Map<String, List<MeetingMiniDto>>>()
            }
        }

    suspend fun getMonthSchedule(year: Int, month: Int): Result<Map<String, List<MeetingMiniDto>>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = ApiClient.client.get("meetings/schedule/month?year=$year&month=$month")
                result.body<Map<String, List<MeetingMiniDto>>>()
            }
        }

    suspend fun getInvitations(): Result<List<InvitationDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = ApiClient.client.get("invitation")
            result.body<List<InvitationDto>>()
        }
    }

    suspend fun respondToInvitation(invitationId: Long, status: InvitationStatus): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = ApiClient.client.put("invitation/respond") {
                    setBody(InvitationRespondDto(invitationId, status))
                }
                result.body<Unit>()
            }
        }
}