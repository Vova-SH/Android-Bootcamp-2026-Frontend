package ru.sicampus.bootcamp2026.data.source


import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.core.Constants
import ru.sicampus.bootcamp2026.domain.entities.InvitationStatus
import ru.sicampus.bootcamp2026.data.dto.invitation.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.invitation.InvitationRespondDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingCreateDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingMiniDto
import ru.sicampus.bootcamp2026.ui.screens.profile.Content
import ru.sicampus.bootcamp2026.data.dto.user.UserDto
import ru.sicampus.bootcamp2026.data.dto.user.UserUpdateDto
import java.time.LocalDate

class MeetingDataSource {
    suspend fun createMeeting(meetingData: MeetingCreateDto): Result<MeetingDto> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = ApiClient.client.post(Constants.MEETING_ENDPOINT + "/create") {
                    setBody(meetingData)
                }

                when (result.status) {
                    HttpStatusCode.OK -> result.body<MeetingDto>()

                    HttpStatusCode.Conflict -> error("Это время занято")
                    else -> error("Ошибка сервера: ${result.status}")
                }
            }
        }

    suspend fun getMeetingById(id: Long): Result<MeetingDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = ApiClient.client.get(Constants.MEETING_ENDPOINT + "/$id")
            result.body<MeetingDto>()
        }
    }

    suspend fun daySchedule(token: String?, day: String): Result<List<MeetingMiniDto>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = ApiClient.client.get(Constants.DAY_SCHEDULE_ENDPOINT + "?day=$day")

                when (response.status) {
                    HttpStatusCode.OK -> response.body<List<MeetingMiniDto>>()

                    else -> error("Ошибка сервера: ${response.status}")
                }
            }
        }

    suspend fun getInvitations(): Result<List<InvitationDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = ApiClient.client.get(Constants.INVITATION_ENDPOINT)
            result.body<List<InvitationDto>>()
        }
    }

    suspend fun respondToInvitation(invitationId: Long, status: InvitationStatus): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = ApiClient.client.put(Constants.INVITATION_ENDPOINT + "/respond") {
                    setBody(InvitationRespondDto(invitationId, status))
                }
                result.body<Unit>()
            }
        }
}

//    suspend fun createMeeting(meetingData: MeetingCreateDto): Result<MeetingDto> = withContext(Dispatchers.IO) {
//        runCatching {
//            val result = ApiClient.client.post(Constants.MEETING_ENDPOINT + "/create") {
//                setBody(meetingData)
//            }
//            result.body<MeetingDto>()
//        }
//    }
//
//    suspend fun getMeetingById(id: Long): Result<MeetingDto> = withContext(Dispatchers.IO) {
//        runCatching {
//            val result = ApiClient.client.get(Constants.MEETING_ENDPOINT + id)
//            result.body<MeetingDto>()
//        }
//    }
//
//    suspend fun getDaySchedule(day: LocalDate): Result<List<MeetingMiniDto>> = withContext(Dispatchers.IO) {
//        runCatching {
//            val result = ApiClient.client.get(Constants.SHEDULE_ENDPOINT + "/day?day=$day")
//            result.body<List<MeetingMiniDto>>()
//        }
//    }
//
//    suspend fun getWeekSchedule(year: Int, week: Int): Result<Map<String, List<MeetingMiniDto>>> =
//        withContext(Dispatchers.IO) {
//            runCatching {
//                val result = ApiClient.client.get(Constants.SHEDULE_ENDPOINT + "/week?year=$year&week=$week")
//                result.body<Map<String, List<MeetingMiniDto>>>()
//            }
//        }
//
//    suspend fun getMonthSchedule(year: Int, month: Int): Result<Map<String, List<MeetingMiniDto>>> =
//        withContext(Dispatchers.IO) {
//            runCatching {
//                val result = ApiClient.client.get(Constants.SHEDULE_ENDPOINT + "/month?year=$year&month=$month")
//                result.body<Map<String, List<MeetingMiniDto>>>()
//            }
//        }
//
//    suspend fun getInvitations(): Result<List<InvitationDto>> = withContext(Dispatchers.IO) {
//        runCatching {
//            val result = ApiClient.client.get("invitation")
//            result.body<List<InvitationDto>>()
//        }
//    }
//
//    suspend fun respondToInvitation(invitationId: Long, status: InvitationStatus): Result<Unit> =
//        withContext(Dispatchers.IO) {
//            runCatching {
//                val result = ApiClient.client.put("invitation/respond") {
//                    setBody(InvitationRespondDto(invitationId, status))
//                }
//                result.body<Unit>()
//            }
//        }
