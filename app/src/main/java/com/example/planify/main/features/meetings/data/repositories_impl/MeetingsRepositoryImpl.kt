package com.example.planify.main.features.meetings.data.repositories_impl

import com.example.planify.main.features.auth.domain.utils.network.AuthenticatedApiClient
import com.example.planify.main.features.meetings.data.dto.create_meeting.CreateMeetingRequestDTO
import com.example.planify.main.features.meetings.data.dto.create_meeting.CreateMeetingResponseDTO
import com.example.planify.main.features.meetings.data.dto.get_meeting.GetMeetingResponseDTO
import com.example.planify.main.features.meetings.data.dto.get_meeting_context.GetMeetingContextResponseDTO
import com.example.planify.main.features.meetings.data.dto.get_my_daily_meetings.GetMyDailyMeetingsDTO
import com.example.planify.main.features.meetings.data.dto.get_my_daily_meetings_short.GetMyDailyMeetingsShortDTO
import com.example.planify.main.features.meetings.data.dto.patch_meeting.PatchMeetingRequestDTO
import com.example.planify.main.features.meetings.domain.entities.Meeting
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.features.meetings.domain.repositories.MeetingsRepository
import com.example.planify.main.features.meetings.domain.schemas.CreateMeetingSchema
import com.example.planify.main.features.meetings.domain.schemas.PatchMeetingSchema
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Singleton
class MeetingsRepositoryImpl @Inject constructor(
    val authenticatedApiClient: AuthenticatedApiClient
) : MeetingsRepository {
    private val meetingsFeaturePath = "/meetings"

    private val getMeetingContextPath = "$meetingsFeaturePath/%d/context"
    private val createMeetingPath = meetingsFeaturePath
    private val getMeetingPath = "$meetingsFeaturePath/%d"
    private val patchMeetingPath = "$meetingsFeaturePath/%d"
    private val getMyDailyMeetingsPath = "$meetingsFeaturePath/my"
    private val getMyDailyMeetingsShortPath = "$meetingsFeaturePath/my/short"

    override suspend fun createMeeting(schema: CreateMeetingSchema): Result<Meeting> = withContext(Dispatchers.IO) {
        val requestDTO = CreateMeetingRequestDTO(
            name = schema.name,
            description = schema.description,
            location = schema.location,
            startsAt = schema.startsAt,
            duration = schema.duration
        )

        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<CreateMeetingResponseDTO> {
                method = HttpMethod.Post
                url { path(createMeetingPath) }
                setBody(requestDTO)
            }

            response.meeting.toEntity()
        }
    }

    override suspend fun fetchMeetingContext(meetingId: Long): Result<MeetingContext> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<GetMeetingContextResponseDTO> {
                method = HttpMethod.Get
                url { path(getMeetingContextPath.format(meetingId)) }
            }

            response.meetingContext.toEntity()
        }
    }

    override suspend fun getMeeting(meetingId: Long): Result<Meeting> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<GetMeetingResponseDTO> {
                method = HttpMethod.Get
                url { path(getMeetingPath.format(meetingId)) }
            }

            response.meeting.toEntity()
        }
    }

    override suspend fun patchMeeting(meetingId: Long, patch: PatchMeetingSchema): Result<Unit> = withContext(Dispatchers.IO) {
        val requestDTO = PatchMeetingRequestDTO(
            name = patch.name,
            description = patch.description,
            location = patch.location,
            startsAt = patch.startsAt,
            duration = patch.duration
        )

        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Patch
                url { path(patchMeetingPath.format(meetingId)) }
                setBody(requestDTO)
            }
        }
    }

    override suspend fun fetchMyDailyMeetings(startDate: LocalDate, endDate: LocalDate): Result<Map<LocalDate, List<MeetingContext>>> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<GetMyDailyMeetingsDTO> {
                method = HttpMethod.Get
                url { path(getMyDailyMeetingsPath) }
                parameter("dateStart", startDate.toString())
                parameter("dateEnd", endDate.toString())
            }

            response.meetings.map { (key, value) ->
                val newKey = Instant.parse(key).atZone(ZoneOffset.UTC).toLocalDate()
                val newValue = value.map { it.toEntity() }
                newKey to newValue
            }.toMap()
        }
    }

    override suspend fun fetchMyDailyMeetingsShort(startDate: LocalDate, endDate: LocalDate): Result<Map<LocalDate, Int>> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<GetMyDailyMeetingsShortDTO> {
                method = HttpMethod.Get
                url { path(getMyDailyMeetingsShortPath) }
                parameter("dateStart", startDate.toString())
                parameter("dateEnd", endDate.toString())
            }

            response.meetings.mapKeys { (key, _) ->
                Instant.parse(key)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate()
            }
        }
    }
}
